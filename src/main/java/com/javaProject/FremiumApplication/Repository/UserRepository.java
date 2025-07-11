package com.javaProject.FremiumApplication.Repository;

import com.javaProject.FremiumApplication.Entity.Users;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends MongoRepository<Users,String> {
    Users findByEmail(String email);
}
