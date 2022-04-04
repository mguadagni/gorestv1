package com.careerdevs.gorestv1.controllers;

import com.careerdevs.gorestv1.models.PostModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping ("/api/post")
public class PostController {

    @Autowired
    Environment env;

    @GetMapping ("/firstpage")
    public PostModel[] getFirstPage(RestTemplate restTemplate) {

        String url = "https://gorest.co.in/public/v2/posts";
        return restTemplate.getForObject(url, PostModel[].class);

    }

    @GetMapping("/{id}")
    public Object getOnePost(RestTemplate restTemplate, @PathVariable("id") int postId) {

        try {

            String url = "https://gorest.co.in/public/v2/posts/" + postId;

            var user = restTemplate.getForObject(url, PostModel.class);

            return user;

        } catch (Exception e){

            System.out.println(e.getClass());
            System.out.println(e.getMessage());

            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);

        }

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteOnePost(RestTemplate restTemplate, @PathVariable("id") int postId) {

        try {

            String url = "https://gorest.co.in/public/v2/posts/" + postId;
            String token = env.getProperty("GOREST_TOKEN");
            url += "?access-token=" + token;

            PostModel deletedPost = restTemplate.getForObject(url, PostModel.class);

            restTemplate.delete(url);

            return new ResponseEntity<>(deletedPost, HttpStatus.OK);

        } catch (Exception e){

            System.out.println(e.getClass());
            System.out.println(e.getMessage());

            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);

        }

    }

    @PostMapping
    public ResponseEntity<Object> postPost (
            RestTemplate restTemplate,
            @RequestBody PostModel newPost
    ) {

        try {

            String url = "https://gorest.co.in/public/v2/posts/";
            String token = env.getProperty("GOREST_TOKEN");
            url += "?access-token=" + token;

            HttpEntity<PostModel> request = new HttpEntity<>(newPost);

            PostModel createdPost = restTemplate.postForObject(url, request, PostModel.class);

            return new ResponseEntity<>(createdPost, HttpStatus.CREATED);

        } catch (Exception e) {

            System.out.println(e.getClass());
            System.out.println(e.getMessage());

            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);

        }

    }

    @PutMapping
    public ResponseEntity<Object> putPost (
            RestTemplate restTemplate,
            @RequestBody PostModel updatePost
    ) {

        try {

            String url = "https://gorest.co.in/public/v2/posts/" + updatePost.getId();
            String token = env.getProperty("GOREST_TOKEN");
            url += "?access-token=" + token;

            HttpEntity<PostModel> request = new HttpEntity<>(updatePost);

            ResponseEntity<PostModel> response = restTemplate.exchange(
                    url,
                    HttpMethod.PUT,
                    request,
                    PostModel.class
            );

            return new ResponseEntity<>(response.getBody(), HttpStatus.CREATED);

        } catch (Exception e){

            System.out.println(e.getClass());
            System.out.println(e.getMessage());

            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);

        }

    }

}
