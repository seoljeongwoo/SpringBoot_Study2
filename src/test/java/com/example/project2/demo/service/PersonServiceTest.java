package com.example.project2.demo.service;

import com.example.project2.demo.controller.dto.PersonDto;
import com.example.project2.demo.domain.Person;
import com.example.project2.demo.domain.dto.Birthday;
import com.example.project2.demo.exception.PersonNotFoundException;
import com.example.project2.demo.exception.RenameNotPermittedException;
import com.example.project2.demo.repository.PersonRepository;
import org.assertj.core.util.Lists;
import org.hibernate.tool.schema.internal.exec.ScriptTargetOutputToFile;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatcher;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PersonServiceTest {
    @InjectMocks
    private PersonService personService;
    @Mock
    private PersonRepository personRepository;

    @Test
    void getPeopleByName(){
        when(personRepository.findByName("martin"))
                .thenReturn(Lists.newArrayList(new Person("martin")));

        List<Person> result = personService.getPeopleByName("martin");

        assertThat(result.size()).isEqualTo(1);
        assertThat(result.get(0).getName()).isEqualTo("martin");
    }

    @Test
    void getPerson(){
        when(personRepository.findById(1L))
                .thenReturn(Optional.of(new Person("martin")));

        Person person = personService.getPerson(1L);
        assertThat(person.getName()).isEqualTo("martin");
    }

    @Test
    void getPersonIfNotFound(){
        when(personRepository.findById(1L))
                .thenReturn(Optional.empty());

        Person person = personService.getPerson(1L);
        assertThat(person).isNull();
    }

    @Test
    void put(){
        personService.put(makePersonDto());

        verify(personRepository, times(1)).save(argThat(new IsPersonWillBeInserted()));
    }
    @Test
    void modifyIfPersonNotFound(){
        when(personRepository.findById(1L))
                .thenReturn(Optional.empty());
        assertThrows(PersonNotFoundException.class, () -> personService.modi(1L,makePersonDto()));
    }
    @Test
    void modifyIfNameIsDiffrent(){
        when(personRepository.findById(1L))
                .thenReturn(Optional.of(new Person("tony")));
        assertThrows(RenameNotPermittedException.class, ()->personService.modi(1L,makePersonDto()));
    }
    @Test
    void modify(){
        when(personRepository.findById(1L))
                .thenReturn(Optional.of(new Person("martin")));
        personService.modi(1L,makePersonDto());

        verify(personRepository, times(1)).save(argThat(new IsPersonWillBeUpdated()));
    }
    @Test
    void modifyByNameIfPersonNotFound(){
        when(personRepository.findById(1L))
                .thenReturn(Optional.empty());

        assertThrows(PersonNotFoundException.class , () ->personService.modi(1L,"daniel"));
    }
    @Test
    void modifyByName(){
        when(personRepository.findById(1L))
                .thenReturn(Optional.of(new Person("martin")));

        personService.modi(1L,"daniel");
        verify(personRepository, times(1)).save(argThat(new IsNameWillBeUpdated()));
    }

    @Test
    void deleteIfPersonNotFound(){
        when(personRepository.findById(1L))
                .thenReturn(Optional.empty());

        assertThrows(PersonNotFoundException.class, () -> personService.delete(1L));
    }

    @Test
    void delete(){
        when(personRepository.findById(1L))
                .thenReturn(Optional.of(new Person("martin")));

        personService.delete(1l);

        verify(personRepository, times(1)).save(argThat(new IsPersonWillBeDeleted()));
    }

    private PersonDto makePersonDto(){
        return PersonDto.of("martin","programming","판교",LocalDate.now(),"programmer","010-1111-2222");
    }
    private static class IsPersonWillBeInserted implements ArgumentMatcher<Person>{

        @Override
        public boolean matches(Person person) {
            return equals(person.getName(),"martin")
                    && equals(person.getHobby(),"programming")
                    && equals(person.getAddress(),"판교")
                    && equals(person.getBirthday(),Birthday.of(LocalDate.now()))
                    && equals(person.getJob(),"programmer")
                    && equals(person.getPhoneNumber(),"010-1111-2222");
        }
        private boolean equals(Object actual, Object expected){
            return expected.equals(actual);
        }
    }

    private static class IsPersonWillBeUpdated implements ArgumentMatcher<Person>{
        @Override
        public boolean matches(Person person){
            return equals(person.getName(),"martin")
                    && equals(person.getHobby(),"programming")
                    && equals(person.getAddress(),"판교")
                    && equals(person.getBirthday(),Birthday.of(LocalDate.now()))
                    && equals(person.getJob(),"programmer")
                    && equals(person.getPhoneNumber(),"010-1111-2222");
        }

        private boolean equals(Object actual, Object expected){
            return expected.equals(actual);
        }
    }

    private static class IsNameWillBeUpdated implements ArgumentMatcher<Person>{
        @Override
        public boolean matches(Person person){
            return person.getName().equals("daniel");
        }
    }

    private static class IsPersonWillBeDeleted implements ArgumentMatcher<Person>{

        @Override
        public boolean matches(Person person) {
            return person.isDeleted();
        }
    }
}