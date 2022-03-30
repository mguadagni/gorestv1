package com.careerdevs.gorestv1.controllers;

import com.careerdevs.gorestv1.models.UserModel;
import com.careerdevs.gorestv1.models.UserModelArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

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

    //(URL / endpoint) GET http://localhost:4444/api/user/page
    @GetMapping ("/page/{pageNum}")
    public Object getPage (
            RestTemplate restTemplate,
            @PathVariable ("pageNum") String pageNumber
    ) {

        try {

            String url = "https://gorest.co.in/public/v2/users?page=" + pageNumber;

            ResponseEntity<UserModel[]> response = restTemplate.getForEntity(url, UserModel[].class);

            UserModel[] firstPageUsers = response.getBody();

            HttpHeaders responseHeaders = response.getHeaders();

            String totalPages = Objects.requireNonNull(responseHeaders.get("X-Pagination-Pages")).get(0);

//            System.out.println("Total Pages: " + totalPages);

            return new ResponseEntity<>(firstPageUsers, HttpStatus.OK);

        } catch (Exception e){

            System.out.println(e.getClass());
            System.out.println(e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);

        }

    }

    //(URL / endpoint) GET http://localhost:4444/api/user/{id}
    @GetMapping ("/{id}")
    public Object getOneUser (
            @PathVariable ("id") String userId,
            RestTemplate restTemplate
    ) {

        try {
            String url = "https://gorest.co.in/public/v2/users/" + userId;
            String apiToken = env.getProperty("GOREST_TOKEN");
            url += "?access-token=" + apiToken;

            var user = restTemplate.getForObject(url, UserModel.class);

            assert user != null;
            System.out.println("Report: \n" + user.generateReport());

            return user;

        } catch (HttpClientErrorException.NotFound exception) {

            return "User could not be found, user #" +userId + " does not exist";

        } catch (Exception exception){

            System.out.println(exception.getClass());
            return exception.getMessage();

        }

    }

    @GetMapping("/all")
        public ResponseEntity getAll(RestTemplate restTemplate) {

            try {

                ArrayList<UserModel> allUsers = new ArrayList<>();

                String url = "https://gorest.co.in/public/v2/users";

                ResponseEntity<UserModel[]> response = restTemplate.getForEntity(url, UserModel[].class);

                allUsers.addAll(Arrays.asList(Objects.requireNonNull(response.getBody())));

                int totalPageNumber = Integer.parseInt(response.getHeaders().get("X-Pagination-Pages").get(0));

                for (int i = 2; i <= totalPageNumber; i++) {
                    String tempUrl = url + "?page=" + i;

                    UserModel[] pageData = restTemplate.getForObject(tempUrl, UserModel[].class);
                    allUsers.addAll(Arrays.asList(Objects.requireNonNull(pageData)));
                }

                return new ResponseEntity(allUsers, HttpStatus.OK);

            } catch (Exception e) {

                System.out.println(e.getClass());
                System.out.println(e.getMessage());

                return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);

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

            url += "?access-token=" + token;

            restTemplate.delete(url);

            return "Successfully deleted user # " + userId;

        } catch (HttpClientErrorException.NotFound exception) {

            return "User could not be deleted, user #" + userId + " does not exist";

        } catch (HttpClientErrorException.Unauthorized exception) {

            return "You are not authorized to delete user #" + userId;

        } catch (Exception exception){

            System.out.println(exception.getClass());
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
