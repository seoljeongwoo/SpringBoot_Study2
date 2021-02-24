package com.example.project2.demo.service;

import com.example.project2.demo.domain.Block;
import com.example.project2.demo.domain.Person;
import com.example.project2.demo.repository.BlockRepository;
import com.example.project2.demo.repository.PersonRepository;
import org.hibernate.tool.schema.internal.exec.ScriptTargetOutputToFile;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class PersonServiceTest {
    @Autowired
    private PersonService personService;

    @Test
    void getPeopleExcludeBlocks(){
        List<Person> result = personService.getPeopleExcludeBlocks();
        result.forEach(System.out::println);
        assertEquals(result.size() ,3 );
        assertEquals(result.get(0).getName(),"martin");
        assertEquals(result.get(1).getName(),"david");
    }

    @Test
    void getPeopleByName(){
        List<Person> result = personService.getPeopleByName("martin");
        assertEquals(result.size() , 1);
        assertEquals(result.get(0).getName(), "martin");
    }

    @Test
    void getPerson(){
        Person person = personService.getPerson(3L);
        assertEquals(person.getName(),"dennis");
    }

}