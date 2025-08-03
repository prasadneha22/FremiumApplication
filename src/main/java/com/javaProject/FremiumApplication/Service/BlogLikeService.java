package com.javaProject.FremiumApplication.Service;

import com.javaProject.FremiumApplication.Entity.Blog;
import com.javaProject.FremiumApplication.Entity.BlogLike;
import com.javaProject.FremiumApplication.Entity.BlogType;
import com.javaProject.FremiumApplication.Repository.BlogLikeRepository;
import com.javaProject.FremiumApplication.Repository.BlogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;


import java.util.NoSuchElementException;

@Service
public class BlogLikeService {

    @Autowired
    private JwtService jwtService;

    @Autowired
    private BlogRepository blogRepository;

    @Autowired
    private BlogLikeRepository blogLikeRepository;

    public void addLike(String blogId, String token) {

        String userId = jwtService.extractUserId(token);

        boolean blogExists = blogRepository.existsById(blogId);
        if(!blogExists){
            throw new NoSuchElementException("Blog not found with id : " + blogId);
        }

        boolean alreadyLiked = blogLikeRepository.existsByUserIdAndBlogId(userId,blogId);
        if(alreadyLiked){
            throw new IllegalStateException("You have Already liked this blog");

        }
        BlogLike like = new BlogLike();
        like.setBlogId(blogId);
        like.setUserId(userId);
        blogLikeRepository.save(like);
    }

    public long getLikeCount(String blogId) {
        return blogLikeRepository.countByBlogId(blogId);
    }

    public long viewLikeCountPublic(String blogId) {
        Blog blog = blogRepository.findById(blogId)
                .orElseThrow(()->new NoSuchElementException("Blog not found with id " + blogId ));

        if(blog.getType() != BlogType.FREE){
            throw new AccessDeniedException("You are not allowed to view likes for this blog. Please login to view Fremium blog.");
        }
        return blogLikeRepository.countByBlogId(blogId);
    }

    public long viewLikeCount(String blogId) {
        if(!blogRepository.existsById(blogId)){
            throw new NoSuchElementException("blog not found with ID: " + blogId);
        }

        return blogLikeRepository.countByBlogId(blogId);
    }
}
