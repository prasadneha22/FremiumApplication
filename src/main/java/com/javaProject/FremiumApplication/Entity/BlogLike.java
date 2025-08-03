package com.javaProject.FremiumApplication.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "blog_likes")
public class BlogLike {

    @Id
    private String id;

    private String blogId;
    private String userId;
}
