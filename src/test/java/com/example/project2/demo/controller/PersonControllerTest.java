package com.example.project2.demo.controller;

import com.example.project2.demo.controller.dto.PersonDto;
import com.example.project2.demo.domain.Person;
import com.example.project2.demo.domain.dto.Birthday;
import com.example.project2.demo.repository.PersonRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.NestedServletException;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@Slf4j
@Transactional
class PersonControllerTest {

    @Autowired
    private PersonController personController;

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MappingJackson2HttpMessageConverter messageConverter;

    private MockMvc mockMvc;

    @BeforeEach
    void beforeEach(){
        mockMvc = MockMvcBuilders.standaloneSetup(personController)
                .setMessageConverters(messageConverter)
                .alwaysDo(print())
                .build();
    }

    @Test
    void getPerson() throws Exception{
        mockMvc.perform(
                MockMvcRequestBuilders.get("/api/person/1"))
         .andExpect(MockMvcResultMatchers.status().isOk())
         .andExpect(jsonPath("$.name").value("martin"))
         .andExpect(jsonPath("$.hobby").isEmpty())
         .andExpect(jsonPath("$.address").isEmpty())
         .andExpect(jsonPath("$.birthday").value("2020-02-10"))
         .andExpect(jsonPath("$.job").isEmpty())
         .andExpect(jsonPath("$.phoneNumber").isEmpty())
         .andExpect(jsonPath("$.deleted").value(false))
         .andExpect(jsonPath("$.age").isNumber())
         .andExpect(jsonPath("$.birthdayToday").isBoolean())
        ;
    }

    @Test
    void postPerson() throws Exception{
        PersonDto personDto = PersonDto.of("martin","programming","판교",LocalDate.now(),"programmer","010-1111-2222");

        mockMvc.perform(
                MockMvcRequestBuilders.post("/api/person")
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJsonString(personDto)))
         .andExpect(MockMvcResultMatchers.status().isCreated());

        Person result = personRepository.findAll(Sort.by(Sort.Direction.DESC, "id")).get(0);

        assertAll(
                () -> assertThat(result.getName()).isEqualTo("martin"),
                () -> assertThat(result.getHobby()).isEqualTo("programming"),
                () -> assertThat(result.getAddress()).isEqualTo("판교"),
                () -> assertThat(result.getBirthday()).isEqualTo(Birthday.of(LocalDate.now())),
                () -> assertThat(result.getJob()).isEqualTo("programmer"),
                () -> assertThat(result.getPhoneNumber()).isEqualTo("010-1111-2222")
        );

    }

    @Test
    void postPersonIfNameIsNull() throws Exception{
        PersonDto personDto = new PersonDto();
        mockMvc.perform(
                MockMvcRequestBuilders.post("/api/person")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(toJsonString(personDto)))
                .andExpect(jsonPath("$.code").value(500))
                .andExpect(jsonPath("$.message").value("알 수 없는 서버 오류가 발생하였습니다"));
    }

    @Test
    void modifyPerson() throws Exception{

        PersonDto personDto = PersonDto.of("martin","programming","판교",LocalDate.now(),"programmer","010-1111-2222");
        mockMvc.perform(
                MockMvcRequestBuilders.put("/api/person/1")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(toJsonString(personDto)))
                .andExpect(MockMvcResultMatchers.status().isOk());

        Person result = personRepository.findById(1L).get();

        assertAll(
                ()->assertEquals(result.getName(),"martin"),
                ()->assertEquals(result.getHobby(),"programming"),
                ()->assertEquals(result.getAddress(),"판교"),
                ()->assertEquals(result.getJob(),"programmer"),
                ()->assertEquals(result.getPhoneNumber(),"010-1111-2222")
        );



    }

    @Test
    void modifyPersonIfNameIsDifferent() throws Exception{

        PersonDto personDto = PersonDto.of("james","programming","판교",LocalDate.now(),"programmer","010-1111-2222");


        mockMvc.perform(
                MockMvcRequestBuilders.put("/api/person/1")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(toJsonString(personDto)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(jsonPath("$.code").value(400))
                .andExpect(jsonPath("$.message").value("이름을 변경 허용하지 않습니다"));
    }

    @Test
    void modifyPersonIfPersonNotFound() throws Exception{
        PersonDto personDto = PersonDto.of("martin","programming","판교",LocalDate.now(), "programmer","010-1111-2222");
        mockMvc.perform(
                MockMvcRequestBuilders.put("/api/person/10")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(toJsonString(personDto)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(jsonPath("$.code").value(400))
                .andExpect(jsonPath("$.message").value("Person Entity가 존재하지 않습니다"));
    }

    @Test
    void modifyName() throws Exception{
        mockMvc.perform(
                MockMvcRequestBuilders.patch("/api/person/1")
                .param("name","martin22"))
                .andExpect(MockMvcResultMatchers.status().isOk());
        assertThat(personRepository.findById(1L).get().getName()).isEqualTo("martin22");
    }

    @Test
    void deletePerson() throws Exception{
        mockMvc.perform(
                MockMvcRequestBuilders.delete("/api/person/1"))
                .andExpect(MockMvcResultMatchers.status().isOk());
        assertFalse(personRepository.findPeopleDeleted().stream().anyMatch(person -> person.getId().equals(1)));

    }

    private String toJsonString(PersonDto personDto) throws JsonProcessingException {
        return objectMapper.writeValueAsString(personDto);
    }
}