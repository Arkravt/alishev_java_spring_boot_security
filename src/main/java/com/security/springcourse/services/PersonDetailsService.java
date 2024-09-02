package com.security.springcourse.services;

import com.security.springcourse.models.Person;
import com.security.springcourse.repositories.PeopleRepository;
import com.security.springcourse.security.PersonDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class PersonDetailsService implements UserDetailsService  {

    @Autowired
    private PeopleRepository peopleRepository;

//    @Autowired
//    public PersonDetailsService(PeopleRepository peopleRepository) {
//        this.peopleRepository = peopleRepository;
//    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Person> person = peopleRepository.findByUsername(username);

        if(person.isEmpty())
            throw new UsernameNotFoundException("User not found !");

//        return new org.springframework.security.core.userdetails.User(
//                person.get().getUsername(),
//                person.get().getPassword(),
//                null);

        return new PersonDetails(person.get());
    }
}
