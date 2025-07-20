package com.javaProject.FremiumApplication.DTO;

import com.javaProject.FremiumApplication.Entity.BlogType;
import lombok.Data;

import java.time.LocalDateTime;
@Data
public class BlogDto {

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
