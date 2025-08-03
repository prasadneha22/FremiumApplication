package com.javaProject.FremiumApplication.Repository;

import com.javaProject.FremiumApplication.Entity.Comment;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.w3c.dom.stylesheets.LinkStyle;

import java.util.List;

public interface CommentRepository extends MongoRepository<Comment,String> {
    List<Comment> findByBlogId(String blogId);
}
