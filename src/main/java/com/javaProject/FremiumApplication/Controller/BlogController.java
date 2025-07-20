package com.javaProject.FremiumApplication.Controller;

import com.javaProject.FremiumApplication.Entity.Blog;
import com.javaProject.FremiumApplication.Service.BlogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController()
@RequestMapping("/api/blogs")
public class BlogController {

    @Autowired
    private BlogService blogService;

    @PostMapping("/create")
    public ResponseEntity<?> createBlog(@RequestHeader("Authorization") String token, @RequestBody Blog blog){
        if(token.startsWith("Bearer ")){
            token = token.substring(7);
        }
        try{
            Blog createdBlog = blogService.createBlog(token,blog);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdBlog);
        }catch (RuntimeException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: " + e.getMessage());
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred.");
        }
    }

    @GetMapping("/public")
    public ResponseEntity<?> getPublicBlogs(){
        return ResponseEntity.ok(blogService.getAllPublicBlogs());
    }
    @GetMapping("/all")
    public ResponseEntity<?> getAllBlogs(@RequestHeader("Authorization") String token){
        if(token.startsWith("Bearer ")){
            token = token.substring(7);
        }
        return ResponseEntity.ok(blogService.getAllBlogs(token));
    }


}
