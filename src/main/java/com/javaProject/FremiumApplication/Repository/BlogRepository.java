package com.javaProject.FremiumApplication.Repository;

import com.javaProject.FremiumApplication.Entity.Blog;
import com.javaProject.FremiumApplication.Entity.BlogType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

//import java.awt.print.Pageable;
import java.util.List;
import java.util.Optional;

public interface BlogRepository extends MongoRepository<Blog,String> {
//    Optional<Blog> findByIdAndType(String id, BlogType type);
//    Page<Blog> findAll(Pageable pageable);
 List<Blog> findByTitleContainingIgnoreCase(String keyword);
//
//    //by userid
//    Page<Blog> findByUserId(String userId, Pageable pageable);
//    Page<Blog> findByType(BlogType type, Pageable pageable);
//    Page<Blog> findByTypeAndCategory(String type, String category, Pageable pageable);

    Page<Blog> findByType(String type, Pageable pageable);
    Page<Blog> findByCategory(String category, Pageable pageable);
    Page<Blog> findByTypeAndCategory(String type, String category, Pageable pageable);
}
