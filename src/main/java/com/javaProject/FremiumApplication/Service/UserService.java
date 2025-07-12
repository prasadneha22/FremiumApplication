package com.javaProject.FremiumApplication.Service;

import com.javaProject.FremiumApplication.DTO.LoginDto;
import com.javaProject.FremiumApplication.Entity.Roles;
import com.javaProject.FremiumApplication.Entity.Users;
import com.javaProject.FremiumApplication.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    private JwtService jwtService;

    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);

    public Users register(Users users) {
        Users existingUser = userRepository.findByEmail(users.getEmail());
        if (existingUser != null) {
            throw new RuntimeException("Email Already Existed!");
        }


        users.setPassword(encoder.encode(users.getPassword()));
        users.setCreatedAt(Instant.now());
        users.setUpdatedAt(Instant.now());
        users.setActive(true);
        users.setRoles(List.of(Roles.USER));


        return userRepository.save(users);
    }


    public Map<String, Object> login(LoginDto loginDto) {
        Map<String, Object> response = new HashMap<>();
        try{
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginDto.getEmail(),loginDto.getPassword())
            );

            if(authentication.isAuthenticated()){
                Users user = userRepository.findByEmail(loginDto.getEmail());
                if(user ==  null){
                    response.put("msg", "User not found!");
                    response.put("status", 404);
                    return response;
                }

                String token = jwtService.generateToken(user);

                Map<String,Object> userData = new HashMap<>();

                userData.put("id",user.getId());
                userData.put("email",user.getEmail());
                userData.put("username",user.getUsername());
                userData.put("fullName",user.getFullName());
                userData.put("role",user.getRoles().get(0));
                userData.put("Bio",user.getBio());
//                userData.put("createdAt",user.getCreatedAt());
//                userData.put("updatedAt",user.getUpdatedAt());

                response.put("msg","User Logged-in Successfully");
                response.put("data",userData);
                response.put("token",token);
                response.put("status",200);
                return response;
            }
        }catch (Exception e){
            response.put("error","Invalid credentials!" +  e.getMessage());
        }
        return response;
    }
}

