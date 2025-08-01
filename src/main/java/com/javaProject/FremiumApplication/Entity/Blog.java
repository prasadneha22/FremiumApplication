package com.javaProject.FremiumApplication.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "Blogs")
public class Blog {

    private String id;
    private String title;
    private String category;
    private String description;
    private String content;
    private BlogType type;

    private String userId;
    private String authorName;
    private LocalDateTime createdAt;

}
