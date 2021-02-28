package com.example.project2.demo.controller;

import com.example.project2.demo.controller.dto.PersonDto;
import com.example.project2.demo.domain.Person;
import com.example.project2.demo.exception.PersonNotFoundException;
import com.example.project2.demo.exception.RenameNotPermittedException;
import com.example.project2.demo.exception.dto.ErrorResponse;
import com.example.project2.demo.repository.PersonRepository;
import com.example.project2.demo.service.PersonService;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping(value = "/api/person")  // class scope에서 모두 적용
@RestController
@Slf4j
public class PersonController {
    @Autowired
    private PersonService personService;

    @GetMapping("/{id}")
    public Person getPerson(@PathVariable Long id){
        return personService.getPerson(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void postPerson(@RequestBody PersonDto personDto){
        personService.put(personDto);
    }

    @PutMapping("/{id}")
    public void modifyPerson(@PathVariable Long id, @RequestBody PersonDto personDto){
            personService.modi(id, personDto);
    }

    @PatchMapping("/{id}")
    public void modifyPerson(@PathVariable Long id, String name){
        personService.modi(id,name);
    }

    @DeleteMapping("/{id}")
    public void deletePerson(@PathVariable Long id){
        personService.delete(id);
    }

    @ExceptionHandler(value = RenameNotPermittedException.class)
    public ResponseEntity<ErrorResponse> handleRenameNoPermittedException(RenameNotPermittedException ex){
        return new ResponseEntity<>(ErrorResponse.of(HttpStatus.BAD_REQUEST, ex.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = PersonNotFoundException.class)
    public ResponseEntity<ErrorResponse> handlePersonNotFoundException(PersonNotFoundException ex){
        return new ResponseEntity<>(ErrorResponse.of(HttpStatus.BAD_REQUEST, ex.getMessage()),HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = RuntimeException.class)
    public ResponseEntity<ErrorResponse> handleRuntimeException(RuntimeException ex){
        log.error("서버 오류 : {}", ex.getMessage());
        return new ResponseEntity<>(ErrorResponse.of(HttpStatus.INTERNAL_SERVER_ERROR, "알 수 없는 서버 오류가 발생하였습니다"), HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
