package com.spring.config;
import com.spring.security.JWTService;
import com.spring.security.MyUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
@Component
public class JwtFilter extends OncePerRequestFilter {
    @Autowired
    private JWTService jwtService;
    @Autowired
    private ApplicationContext context;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");
        String token = null;
        String username = null;
        // Extract the token from the Authorization header
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            token = authHeader.substring(7);  // Get the token part after "Bearer "
            username = jwtService.extractUserName(token);  // Extract username from the token
        }
        // If a valid username exists and there's no authentication in the context
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            MyUserDetailsService userDetailsService = context.getBean(MyUserDetailsService.class);
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);  // Load user details
            // If the token is valid, set the authentication in the SecurityContext
            if (jwtService.validateToken(token, userDetails)) {
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                // Set the request details for authentication
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);  // Set the authentication in context
            }
        }
        filterChain.doFilter(request, response);  // Continue with the filter chain
    }
}









