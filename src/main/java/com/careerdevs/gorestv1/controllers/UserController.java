package com.careerdevs.gorestv1.controllers;

import com.careerdevs.gorestv1.models.UserModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
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
            String apiToken = env.getProperty("GOREST_TOKEN");
            url += "?access-token=" + apiToken;

            return restTemplate.getForObject(url, Object.class);

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

    @PostMapping ("/qp")
    public Object postUserQueryParam (
            @RequestParam ("name") String name,
            @RequestParam ("email") String email,
            @RequestParam ("gender") String gender,
            @RequestParam ("status") String status,
            RestTemplate restTemplate

    ) {

        try {

            String url = "https://gorest.co.in/public/v2/users/";
            String token = env.getProperty("GOREST_TOKEN");
            url += "?access-token=" + token;

            UserModel newUser = new UserModel (name, email, gender, status);

            System.out.println("Data to be sent:\n" + newUser);

            HttpEntity<UserModel> request = new HttpEntity<>(newUser);

            return restTemplate.postForEntity(url, request, UserModel.class);

        } catch (Exception exception) {
            System.out.println(exception.getClass());
            return exception.getMessage();
        }

    }

    @PostMapping ("/")
    public ResponseEntity postUser (
            RestTemplate restTemplate,
            @RequestBody UserModel newUser
    ) {
        try {

            String url = "https://gorest.co.in/public/v2/users/";
            String token = env.getProperty("GOREST_TOKEN");
            url += "?access-token=" + token;

            HttpEntity<UserModel> request = new HttpEntity<>(newUser);

            return restTemplate.postForEntity(url, request, UserModel.class);

        } catch (Exception e) {
            System.out.println(e.getClass() + " \n " + e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping ("/")
    public ResponseEntity putUser (
            RestTemplate restTemplate,
            @RequestBody UserModel updateData
    ) {
        try {

            String url = "https://gorest.co.in/public/v2/users/" + updateData.getId();
            String token = env.getProperty("GOREST_TOKEN");
            url += "?access-token=" + token;

            HttpEntity<UserModel> request = new HttpEntity<>(updateData);

            ResponseEntity<UserModel> response = restTemplate.exchange(
                    url,
                    HttpMethod.PUT,
                    request,
                    UserModel.class
            );

            return new ResponseEntity(response.getBody(), HttpStatus.OK);

        } catch(HttpClientErrorException.UnprocessableEntity e) {

            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);

        } catch (Exception e) {
            System.out.println(e.getClass() + " \n " + e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
