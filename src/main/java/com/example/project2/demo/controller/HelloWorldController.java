package com.example.project2.demo.controller;

import com.example.project2.demo.exception.dto.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@Controller
@ResponseBody
@RestController
public class HelloWorldController {

    @GetMapping(value = "/api/helloWorld")
    public String helloWorld(){
        return "HelloWorld";
    }
    

    @GetMapping(value = "/api/helloException")
    public String helloException(){
        throw new RuntimeException("Hello RuntimeException");
    }
}
