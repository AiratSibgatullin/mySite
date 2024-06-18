package com.example.mySite.repositories;

import com.example.mySite.models.Person;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.Optional;

public interface PeopleRepository extends JpaRepository<Person, Integer> {
    Optional<Person> findByEmail (String email);
}
