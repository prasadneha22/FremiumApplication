package com.javaProject.FremiumApplication.Controller;

import com.javaProject.FremiumApplication.Service.BlogLikeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/blogs")
public class BlogLikeController {

    @Autowired
    private BlogLikeService blogLikeService;

    @PostMapping("/{blogId}/like")
    public ResponseEntity<?> likeBlog(@PathVariable String blogId, @RequestHeader("Authorization") String token){
        if(token.startsWith("Bearer ")){
            token = token.substring(7);
        }
        try{
            blogLikeService.addLike(blogId,token);
            long likeCount = blogLikeService.getLikeCount(blogId);
            return ResponseEntity.ok(Map.of("message","Blog likes Successfully", "likeCount",likeCount));
        }catch (IllegalArgumentException ex){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        }catch (NoSuchElementException ex){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Blog not found");
        }catch (Exception ex){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(("Something went wrong"));
        }

    }

    @GetMapping("/public/{blogId}/likes")
    public ResponseEntity<?> getLikesPublic(@PathVariable String blogId){
        long count = blogLikeService.viewLikeCountPublic(blogId);
        return ResponseEntity.ok(Map.of("likeCount",count));

    }

    @GetMapping("/{blogId}/likes")
    public ResponseEntity<?> getLikes(@PathVariable String blogId, @RequestHeader("Authorization") String token){
        if(token.startsWith("Bearer ")){
            token = token.substring(7);
        }

        long count  = blogLikeService.viewLikeCount(blogId);
        return ResponseEntity.ok(Map.of("likeCount",count));
    }

}
