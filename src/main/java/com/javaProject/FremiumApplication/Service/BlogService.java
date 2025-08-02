package com.javaProject.FremiumApplication.Service;

import com.javaProject.FremiumApplication.DTO.BlogPageRequest;
import com.javaProject.FremiumApplication.Entity.Blog;
import com.javaProject.FremiumApplication.Entity.BlogType;
import com.javaProject.FremiumApplication.Entity.Users;
import com.javaProject.FremiumApplication.Repository.BlogRepository;
import com.javaProject.FremiumApplication.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


import java.time.LocalDateTime;
import java.util.List;
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

    public List<Blog> getAllBlogs(String token) {
        jwtService.extractUserId(token);

        return blogRepository.findAll();
    }

    public Blog getPublicBlogById(String id) {
        return blogRepository.findById(id)
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

    public void deleteBlogById(String token, String id) {
        String userId= jwtService.extractUserId(token);
        String userRole = jwtService.extractUserRole(token);

        if(!"USER".equalsIgnoreCase(userRole)){
            throw new RuntimeException("Only users can delete blogs");
        }

        Blog blog = blogRepository.findById(id)
                .orElseThrow(()->new RuntimeException("Blog not found with id: " + id));

        if(!userId.equals(blog.getUserId())){
            throw new RuntimeException("you are not authorize to delete this blog");
        }

        blogRepository.deleteById(id);
    }

    public List<Blog> searchBlogsByKeyword(String token, String searchKeyword) {
        jwtService.extractUserId(token);
        if(searchKeyword == null || searchKeyword.trim().isEmpty()){
            return blogRepository.findAll();
        }
        return blogRepository.findByTitleContainingIgnoreCase(searchKeyword);
    }

    public Page<Blog> getPaginatedBlogs(String token, BlogPageRequest request) {
        jwtService.extractUserId(token);

        int page = request.getPage() != null ? request.getPage() : 0;
        int size = request.getSize() !=null ? request.getSize() : 10;
        String sortBy = request.getSortBy() !=null ? request.getSortBy() : "createdAt";
        Sort.Direction direction = request.getSortDirection() !=null ? Sort.Direction.fromString(request.getSortDirection()) : Sort.Direction.DESC;

        Pageable pageable = PageRequest.of(page,size,Sort.by(direction,sortBy));

        if (request.getType() != null && request.getCategory() != null) {
            return blogRepository.findByTypeAndCategory(request.getType(), request.getCategory(), pageable);
        } else if (request.getType() != null) {
            return blogRepository.findByType(request.getType(), pageable);
        } else if (request.getCategory() != null) {
            return blogRepository.findByCategory(request.getCategory(), pageable);
        } else {
            return blogRepository.findAll(pageable);
        }
    }
}
