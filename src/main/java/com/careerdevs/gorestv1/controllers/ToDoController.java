package com.careerdevs.gorestv1.controllers;

import com.careerdevs.gorestv1.models.PostModel;
import com.careerdevs.gorestv1.models.ToDoModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping ("/api/todo")
public class ToDoController {

    @Autowired
    Environment env;

    @GetMapping ("/firstpage")
    public ToDoModel[] getFirstPage(RestTemplate restTemplate) {

        String url = "https://gorest.co.in/public/v2/todos";
        return restTemplate.getForObject(url, ToDoModel[].class);

    }

    @GetMapping("/{id}")
    public Object getOneToDo(RestTemplate restTemplate, @PathVariable("id") int toDoId) {

        try {

            String url = "https://gorest.co.in/public/v2/todos/" + toDoId;

            var user = restTemplate.getForObject(url, ToDoModel.class);

            return user;

        } catch (Exception e){

            System.out.println(e.getClass());
            System.out.println(e.getMessage());

            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);

        }

    }

    @DeleteMapping ("/{id}")
    public ResponseEntity<Object> deleteOneToDo(RestTemplate restTemplate, @PathVariable("id") int toDoId) {

        try {

            String url = "https://gorest.co.in/public/v2/todos/" + toDoId;
            String token = env.getProperty("GOREST_TOKEN");
            url += "?access-token=" + token;

            ToDoModel deletedToDo = restTemplate.getForObject(url, ToDoModel.class);

            restTemplate.delete(url);

            return new ResponseEntity<>(deletedToDo, HttpStatus.OK);

        } catch (Exception e) {

            System.out.println(e.getClass());
            System.out.println(e.getMessage());

            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);

        }

    }

}
