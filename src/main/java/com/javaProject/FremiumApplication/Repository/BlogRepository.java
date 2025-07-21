package com.javaProject.FremiumApplication.Repository;

import com.javaProject.FremiumApplication.Entity.Blog;
import com.javaProject.FremiumApplication.Entity.BlogType;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface BlogRepository extends MongoRepository<Blog,String> {
    Optional<Blog> findByIdAndType(String id, BlogType type);
}
