package com.javaProject.FremiumApplication.Controller;

import com.javaProject.FremiumApplication.DTO.LoginDto;
import com.javaProject.FremiumApplication.Entity.Users;
import com.javaProject.FremiumApplication.Service.UserService;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController()
@RequestMapping("/auth")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<Users> register(@RequestBody Users users){
        try{
            Users registerUser = userService.register(users);
            return ResponseEntity.status(HttpStatus.CREATED).body(registerUser);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String,Object>> login(@RequestBody LoginDto loginDto){

        Map<String,Object> response = userService.login(loginDto);
        if(response.containsKey("Error")){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
        return ResponseEntity.ok(response);
    }
}
