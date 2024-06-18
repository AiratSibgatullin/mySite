package com.example.mySite.servises;

import com.example.mySite.models.Person;
import com.example.mySite.repositories.PeopleRepository;
import com.example.mySite.security.PersonDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PersonDetailService implements UserDetailsService {
    private final PeopleRepository peopleRepository;

    @Autowired
    public PersonDetailService(PeopleRepository peopleRepository) {
        this.peopleRepository = peopleRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        Optional<Person> personOptional = peopleRepository.findByEmail(email);
        if (personOptional.isEmpty()) {
            throw new UsernameNotFoundException("Пользователь с указанным email не найден");
        }
        return new PersonDetails(personOptional.get());
    }
}
