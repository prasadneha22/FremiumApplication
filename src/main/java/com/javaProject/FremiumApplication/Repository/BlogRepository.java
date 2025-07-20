package com.javaProject.FremiumApplication.Repository;

import com.javaProject.FremiumApplication.Entity.Blog;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface BlogRepository extends MongoRepository<Blog,String> {
}
