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

    @GetMapping("/public/{id}")
    public ResponseEntity<?> getPublicBlogsById(@PathVariable String id){
        Blog blog = blogService.getPublicBlogById(id);
        return ResponseEntity.ok(blog);
    }

    @GetMapping("/all/{id}")
    public ResponseEntity<?> getAllBlogsById(@RequestHeader("Authorization") String token ,@PathVariable String id){
        if(token.startsWith("Bearer ")){
            token = token.substring(7);
        }
        Blog blog = blogService.getAllBlogsById(id,token);
        return ResponseEntity.ok(blog);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateBlogById(@RequestHeader("Authorization") String token, @PathVariable String id, @RequestBody Blog updatedData){
        if(token.startsWith("Bearer ")){
            token = token.substring(7);
        }

        Blog updatedBlog = blogService.updateBlogById(id, token, updatedData);
        return ResponseEntity.ok(updatedBlog);
    }



}
