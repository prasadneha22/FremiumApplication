package com.javaProject.FremiumApplication.Service;

import com.javaProject.FremiumApplication.Entity.UserPrincipal;
import com.javaProject.FremiumApplication.Entity.Users;
import com.javaProject.FremiumApplication.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class MyUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Users user1 = userRepository.findByEmail(email);

        System.out.println("Email: " + user1.getEmail());
//        System.out.println("Role: " + user1.getRole());

        if(user1==null){
            throw new UsernameNotFoundException("User not found!");
        }
        return new UserPrincipal(user1);
    }

}
