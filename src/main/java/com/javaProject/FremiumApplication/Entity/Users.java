package com.javaProject.FremiumApplication.Entity;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
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

    @Indexed(unique = true)
    @NotBlank(message = "Username is required")
    private String username;

    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    private String email;

    @NotBlank(message ="Password is required")
    private String password;

    @NotBlank(message = "Full name is re")
    private String fullName;
    private String bio;

    private String profileImageUrl;

    @Builder.Default
    private Roles roles = Roles.USER;

    private boolean isActive;
    private Instant createdAt;
    private Instant updatedAt;

}
