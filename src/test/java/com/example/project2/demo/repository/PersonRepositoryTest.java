package com.example.project2.demo.repository;

import com.example.project2.demo.domain.Person;
import com.example.project2.demo.domain.dto.Birthday;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class PersonRepositoryTest {
    @Autowired
    private PersonRepository personRepository;

    @Test
    void crud(){
        Person person = new Person();
        person.setName("john");
        person.setAge(10);
        person.setBloodType("A");

        personRepository.save(person);

        List<Person> people = personRepository.findByName("john");

        assertEquals(people.get(0).getName(),"john");
        assertEquals(people.get(0).getAge(), 10);
        assertEquals(people.get(0).getBloodType() , "A");
    }

    @Test
    void constructorTest(){
        Person person = new Person("martin",10,"A");
    }

    @Test
    void findByBloodType(){
        List<Person> result = personRepository.findByBloodType("A");
        assertEquals(result.size() , 2);
        assertEquals(result.get(0).getName(), "martin");
        assertEquals(result.get(1).getName(), "benny");
    }

    @Test
    void findByBirthdayBetween(){

        List<Person> result=  personRepository.findByMonthOfBirthday(2);
        assertEquals(result.size() , 3);
        assertEquals(result.get(0).getName(),"martin");
        assertEquals(result.get(2).getName(),"benny");

    }
    private void givenPerson(String name, int age, String bloodType){
        givenPerson(name,age,bloodType,null);
    }

    private void givenPerson(String name, int age, String bloodType, LocalDate birthday){
        Person person = new Person(name, age, bloodType);
        person.setBirthday(new Birthday(birthday));
        personRepository.save(person);
    }
}