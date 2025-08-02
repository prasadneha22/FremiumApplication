package com.javaProject.FremiumApplication.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BlogPageRequest {
    private Integer page;
    private Integer size;
    private String sortBy;
    private String sortDirection;
    private String type;
    private String category;
}
