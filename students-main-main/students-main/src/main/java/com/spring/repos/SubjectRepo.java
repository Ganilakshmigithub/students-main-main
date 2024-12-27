package com.spring.repos;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.spring.entities.Subject;
import org.springframework.stereotype.Repository;

@Repository
public interface SubjectRepo extends JpaRepository<Subject,Integer> {

    @Query("SELECT s FROM Subject s WHERE s.name =:name")
    List<Subject> findSubByName(String name);
    
}
