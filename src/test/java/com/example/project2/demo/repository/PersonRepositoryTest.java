package com.example.project2.demo.repository;

import com.example.project2.demo.domain.Person;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class PersonRepositoryTest {
    @Autowired
    private PersonRepository personRepository;

    @Test
    void crud(){
        Person person = new Person();
        person.setName("martin");
        person.setAge(10);
        person.setBloodType("A");

        personRepository.save(person);

        System.out.println(personRepository.findAll());

        List<Person> people = personRepository.findAll();
        assertEquals(people.get(0).getName(),"martin");
        assertEquals(people.get(0).getAge(), 10);
        assertEquals(people.get(0).getBloodType() , "A");
    }

    @Test
    void constructorTest(){
        Person person = new Person("martin",10);
    }

    @Test
    void hashCodeAndEquals(){
        Person person1 = new Person("martin",10);
        Person person2 = new Person("martin",10);
        System.out.println(person1.equals(person2));
        System.out.println(person1.hashCode());
        System.out.println(person2.hashCode());

        Map<Person, Integer> map = new HashMap<>();
        map.put(person1,person1.getAge());

        System.out.println(map);
        System.out.println(map.get(person2));
    }
}