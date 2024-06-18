package com.example.mySite.servises;

import com.example.mySite.models.Person;
import com.example.mySite.models.Role;
import com.example.mySite.repositories.PeopleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class PeopleService {
    private final PeopleRepository peopleRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public PeopleService(PeopleRepository peopleRepository, PasswordEncoder passwordEncoder) {
        this.peopleRepository = peopleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Optional<Person> findByEmail(String email) {
        Optional<Person> personOptional = peopleRepository.findByEmail(email);
        return personOptional;
    }

    public List<Person> findAll() {
        return peopleRepository.findAll(Sort.by("id"));
    }

    public Person findOne(int id) {
        Optional<Person> personOptional = peopleRepository.findById(id);
        return personOptional.orElse(null);
    }

    @Transactional
    public void savePerson(Person person) {
        person.setEmail(person.getEmail());
        person.setRole(Role.ROLE_USER);
        person.setPassword(passwordEncoder.encode(person.getPassword()));
        peopleRepository.save(person);
    }

    @Transactional
    public void update(int id, Person updatedPerson) {
        updatedPerson.setId(id);
        updatedPerson.setEmail(updatedPerson.getEmail());
        peopleRepository.save(updatedPerson);
    }

    @Transactional
    public void delete(int id) {
        peopleRepository.deleteById(id);
    }
}
