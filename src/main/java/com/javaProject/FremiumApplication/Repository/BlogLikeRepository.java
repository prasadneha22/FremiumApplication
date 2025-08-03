package com.javaProject.FremiumApplication.Repository;

import com.javaProject.FremiumApplication.Entity.BlogLike;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface BlogLikeRepository extends MongoRepository<BlogLike,String> {
    boolean existsByUserIdAndBlogId(String userId, String BlogId);

    long countByBlogId(String blogId);
}
