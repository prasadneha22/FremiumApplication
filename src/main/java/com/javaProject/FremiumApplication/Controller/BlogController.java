package com.javaProject.FremiumApplication.Controller;

import com.javaProject.FremiumApplication.DTO.BlogPageRequest;
import com.javaProject.FremiumApplication.Entity.Blog;
import com.javaProject.FremiumApplication.Entity.Comment;
import com.javaProject.FremiumApplication.Service.BlogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

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
        List<Blog> blogs = blogService.getAllBlogs(token);
        return ResponseEntity.ok(blogs);
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

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteBlogById(@RequestHeader("Authorization") String token, @PathVariable String id){
        if(token.startsWith("Bearer ")){
            token = token.substring(7);
        }
        try{
            blogService.deleteBlogById(token,id);
            return ResponseEntity.ok("Blog deleted successfully.");
        }catch(RuntimeException e){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected occurred.");
        }
    }

    //search filter
    @GetMapping("/search")
    public ResponseEntity<?> searchBlogs(@RequestHeader("Authorization") String token, @RequestParam(required = false) String searchKeyword){
        if(token.startsWith("Bearer ")){
            token = token.substring(7);
        }

        List<Blog> blogs = blogService.searchBlogsByKeyword(token,searchKeyword);
        return ResponseEntity.ok(blogs);
    }

    @PostMapping("/paginated")
    public ResponseEntity<?> getPaginatedBlogs(@RequestHeader("Authorization") String token, @RequestBody BlogPageRequest request){
        if(token.startsWith("Bearer ")){
            token = token.substring(7);
        }
        Page<Blog> paginatedBlogs = blogService.getPaginatedBlogs(token,request);
        return ResponseEntity.ok(paginatedBlogs);
    }

    @PostMapping("/{blogId}/comment")
    public ResponseEntity<?> addComment(@PathVariable String blogId, @RequestHeader("Authorization") String token, @RequestBody String text){
        if(token.startsWith("Bearer ")){
            token = token.substring(7);
        }
        try{
            blogService.addComment(blogId,token,text);
            return ResponseEntity.ok("Comment added");
        }catch (IllegalArgumentException ex){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        }catch (NoSuchElementException ex){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Blog not found");
        }catch (Exception ex){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(("Something went wrong"));
        }
    }

    @GetMapping("/public/{blogId}/comments")
    public ResponseEntity<?> getComments(@PathVariable String blogId){
        try{
            List<Comment> comments = blogService.getComments(blogId);
            return ResponseEntity.ok(comments);
        }catch (IllegalArgumentException ex){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        }catch (NoSuchElementException ex){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Blog not found");
        }catch (Exception ex){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(("Something went wrong"));
        }

    }




}
