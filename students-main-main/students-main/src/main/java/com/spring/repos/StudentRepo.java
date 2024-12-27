package com.spring.repos;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.spring.entities.Students;
import org.springframework.stereotype.Repository;


@Repository
public interface StudentRepo extends JpaRepository<Students,Integer>{

    List<Students> findByName(String name);

    List<Students> findByAge(int age);

    List<Students> findByGender(String gender);
    
}