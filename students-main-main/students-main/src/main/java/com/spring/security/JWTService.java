package com.spring.security;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import javax.crypto.SecretKey;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
@Service
public class JWTService {
    private final String secretKey;
    public JWTService() {
        // Generate a secure secret key for signing JWTs (can also be externally configured)
        SecretKey key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
        this.secretKey = Base64.getEncoder().encodeToString(key.getEncoded());
    }
    // Generate a JWT token with username and roles
    public String generateToken(String username, List<String> roles) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("roles", roles);
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(username)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10)) // Token valid for 10 hours
                .signWith(getKey())  // Signing the token with the key
                .compact();
    }
    private SecretKey getKey() {
        return Keys.hmacShaKeyFor(Base64.getDecoder().decode(secretKey));  // Ensure you're using the same key for signing and validating
    }
    // Extract the username from the JWT
    public String extractUserName(String token) {
        return extractClaim(token, Claims::getSubject);
    }
    // Extract roles from the JWT
    public List<String> extractRoles(String token) {
        return extractClaim(token, claims -> claims.get("roles", List.class));
    }
    // Extract a specific claim from the JWT
    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }
    // Extract all claims from the JWT
    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .setSigningKey(getKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
    // Validate the token based on username and roles
    public boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUserName(token);
        final List<String> tokenRoles = extractRoles(token);
        final List<String> userRoles = userDetails.getAuthorities().stream()
                .map(authority -> authority.getAuthority())
                .collect(Collectors.toList());
        boolean isUsernameValid = username.equals(userDetails.getUsername());
        boolean areRolesValid = new HashSet<>(tokenRoles).equals(new HashSet<>(userRoles));  // Check roles are exactly the same
        return isUsernameValid && !isTokenExpired(token) && areRolesValid;
    }
    // Check if the token is expired
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }
    // Extract the expiration date from the token
    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }
}

