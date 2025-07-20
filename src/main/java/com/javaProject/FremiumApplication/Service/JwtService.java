package com.javaProject.FremiumApplication.Service;

import com.javaProject.FremiumApplication.Entity.Users;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.NoSuchAlgorithmException;
import java.util.*;

import java.util.function.Function;

@Service
public class JwtService {

    private String secretKey = "";

    public JwtService(){
        try{
            KeyGenerator keyGen = KeyGenerator.getInstance("HmacSHA256");
            keyGen.init(256);
            SecretKey sk = keyGen.generateKey();
            secretKey = Base64.getEncoder().encodeToString(sk.getEncoded());
        }catch (NoSuchAlgorithmException e){
            throw new RuntimeException(e);
        }
    }

    public String generateToken(Users users){
        Map<String, Object> claims = new HashMap<>();
        claims.put("id",users.getId());
        claims.put("email",users.getEmail());
        claims.put("fullName",users.getFullName());

//        claims.put("createdAt",users.getCreatedAt());
//        claims.put("updatedAt",users.getUpdatedAt());
        claims.put("role",users.getRoles());

        return Jwts.builder()
                .claims(claims)
                .subject(users.getEmail())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + 86400000))
                .signWith(getKey())
                .compact();
    }

    private SecretKey getKey(){
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
    public String extractEmail(String token) {

        return extractClaim(token, Claims::getSubject);
    }
    public String extractUserId(String token){
        return extractClaim(token,claims -> claims.get("id").toString());
    }

    public String extractUserRole(String token){
        return extractClaim(token,claims -> claims.get("role").toString());
    }

    private <T> T extractClaim(String token, Function<Claims,T> claimResolver){
        final Claims claims = extractAllClaims(token);
        return claimResolver.apply(claims);
    }

    private Claims extractAllClaims(String token){
        return Jwts.parser()
                .verifyWith(getKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public boolean validateToken(String token, UserDetails userDetails) {
        final String email = extractEmail(token);
        final Date expiration = extractExpiration(token);

        System.out.println("extracted email: " + email);
        System.out.println("user details" + userDetails.getUsername());
        System.out.println("token expired at: " + expiration);
        System.out.println("Current time : " + new Date());
        return (email.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    private boolean isTokenExpired(String token) {

        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {

        return extractClaim(token, Claims::getExpiration);
    }
}
