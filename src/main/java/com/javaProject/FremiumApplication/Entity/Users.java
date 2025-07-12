package com.javaProject.FremiumApplication.Entity;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.List;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "users")
public class Users {

    @Id
    private String id;

    private String username;
    private String email;
    private String password;
    private String fullName;
    private String bio;
    private String profileImageUrl;

    @Builder.Default
    private List<Roles> roles = List.of(Roles.USER);

    private boolean isActive;
    private Instant createdAt;
    private Instant updatedAt;

}
