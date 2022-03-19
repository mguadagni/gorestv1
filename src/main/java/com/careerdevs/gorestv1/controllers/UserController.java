package com.careerdevs.gorestv1.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping ("/api/user")
public class UserController {

    @Autowired
    Environment env;

    //URL / endpoint http://localhost:4444/api/user/token
    @GetMapping ("/token")
    public String getToken () {
        return env.getProperty("GOREST_TOKEN");
    }

    @GetMapping ("/{id}")
    public Object getOneUser (
            @PathVariable ("id") String userId,
            RestTemplate restTemplate
    ) {

        try {
            String url = "https://gorest.co.in/public/v2/users/" + userId;

            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(env.getProperty("GOREST_TOKEN"));
            HttpEntity request = new HttpEntity(headers);

            return restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    request,
                    Object.class
            );

        } catch (Exception exception){
            return "404: No user exists with the ID " + userId;
        }


    }

    @DeleteMapping ("/{id}")
    public Object deleteOneUser (
            @PathVariable ("id") String userId,
            RestTemplate restTemplate
    ) {

        try {

            String url = "https://gorest.co.in/public/v2/users/" + userId;
            String token = env.getProperty("GOREST_TOKEN");
            HttpHeaders headers = new HttpHeaders();

            headers.setBearerAuth(token);

            HttpEntity request = new HttpEntity(headers);

            restTemplate.exchange(
                    url,
                    HttpMethod.DELETE,
                    request,
                    Object.class
            );

            return "Successfully deleted user # " + userId;

        } catch (Exception exception){
            return exception.getMessage();
        }

    }

}
