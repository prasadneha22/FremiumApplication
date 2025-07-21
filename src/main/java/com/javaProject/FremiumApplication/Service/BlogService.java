package com.javaProject.FremiumApplication.Service;

import com.javaProject.FremiumApplication.Entity.Blog;
import com.javaProject.FremiumApplication.Entity.BlogType;
import com.javaProject.FremiumApplication.Entity.Users;
import com.javaProject.FremiumApplication.Repository.BlogRepository;
import com.javaProject.FremiumApplication.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

@Service
public class BlogService {

    @Autowired
    private JwtService jwtService;

    @Autowired
    private BlogRepository blogRepository;

    @Autowired
    private UserRepository userRepository;

    public Blog createBlog(String token, Blog blog) {

        String userId = jwtService.extractUserId(token);
        String userRole = jwtService.extractUserRole(token);

        if(!"USER".equalsIgnoreCase(userRole)){
            throw new RuntimeException("Only user can create blog");
        }
        Users users = userRepository.findById(userId)
                .orElseThrow(()->new RuntimeException("User not found!!"));

        if(blog.getType() == null){
            throw new RuntimeException("Blog type is required. Allowed values: FREE, FREMIUM");
        }

        blog.setUserId(userId);
        blog.setAuthorName(users.getFullName());
        blog.setCreatedAt(LocalDateTime.now());
        return blogRepository.save(blog);


    }

    public Object getAllPublicBlogs() {
        return blogRepository.findAll()
                .stream()
                .filter(blog -> blog.getType() == BlogType.FREE)
                .collect(Collectors.toList());
    }

    public Object getAllBlogs(String token) {
        jwtService.extractUserId(token);

        return blogRepository.findAll();
    }

    public Blog getPublicBlogById(String id) {
        return blogRepository.findByIdAndType(id,BlogType.FREE)
                .orElseThrow(()->new RuntimeException("Blog not found with ID : " + id));
    }


    public Blog getAllBlogsById(String id, String token) {
        String userId = jwtService.extractUserId(token);

        return blogRepository.findById(id)
                .orElseThrow(()->new RuntimeException("blog not found with ID : " + id));
    }

    public Blog updateBlogById(String id, String token, Blog updatedData) {

        String userid = jwtService.extractUserId(token);

        Blog existingBlog = blogRepository.findById(id)
                .orElseThrow(()-> new RuntimeException("Blog not found with id : " +  id));

        if(!existingBlog.getUserId().equals(userid)){
            throw new RuntimeException("you are not authorize to update this blog");
        }

        existingBlog.setTitle(updatedData.getTitle());
        existingBlog.setDescription(updatedData.getDescription());
        existingBlog.setContent(updatedData.getContent());
        existingBlog.setCategory(updatedData.getCategory());
        existingBlog.setType(updatedData.getType());

        return blogRepository.save(existingBlog);
    }
}
