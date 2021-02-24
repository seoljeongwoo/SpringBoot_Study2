package com.example.project2.demo.service;

import com.example.project2.demo.domain.Block;
import com.example.project2.demo.domain.Person;
import com.example.project2.demo.repository.BlockRepository;
import com.example.project2.demo.repository.PersonRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class PersonService {

    @Autowired
    private PersonRepository personRepository;
    @Autowired
    private BlockRepository blockRepository;

    public List<Person> getPeopleExcludeBlocks(){
        return personRepository.findByBlockIsNull();

    }

    public List<Person> getPeopleByName(String name){
        return personRepository.findByName(name);
    }

    @Transactional(readOnly = true)
    public Person getPerson(Long id){
//        Person person = personRepository.findById(id).get();

        Person person = personRepository.findById(id).orElse(null);

        log.info("person : {}",person);

        return person;
    }

    @Transactional
    public void put(Person person){
        personRepository.save(person);
    }

}
