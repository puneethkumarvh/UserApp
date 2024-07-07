package com.example.userCrud.services;

import com.example.userCrud.services.auth.MyCustomUserDetailsService;


import io.jsonwebtoken.Claims;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtTokenService {
    public String SECRET_KEY = "6fa42fdfb68d851c11a616c25a6eb06224a53800cf0e405db68f71861f98526d";
    private Date CURRENT_TIME = new Date(System.currentTimeMillis());
    private Date EXPIRATION_TIME = new Date(System.currentTimeMillis() + 1000 * 60 * 60 *24);


    public String extractUsername(String token){
        return extractClaim(token, Claims::getSubject);
    }
    // END OF EXTRACT USERNAME FROM TOKEN METHOD.

    public Date extractExpiration(String token){
        return extractClaim(token, Claims::getExpiration);
    }
    // END OF EXTRACT EXPIRATION DATE FROM TOKEN METHOD.

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver){
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }
    // END OF EXTRACT CLAIMS FROM TOKEN METHOD.

    public Claims extractAllClaims(String token){
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build().parseClaimsJws(token).getBody();
    }
    // END OF EXTRACT ALL CLAIMS FROM TOKEN METHOD.

    private Key getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }
    // END OF GET SIGNING KEY METHOD.

    private Boolean isTokenExpired(String token){
        return extractExpiration(token).before(new Date());
    }
    // END OF CHECK IF TOKEN IS EXPIRED FROM TOKEN METHOD.

    public String generateToken(UserDetails userDetails){
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, userDetails.getUsername());
    }
    // END OF GENERATE TOKEN METHOD.

    private String createToken(Map<String, Object> claims, String subject){
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(CURRENT_TIME)
                .setExpiration(EXPIRATION_TIME)
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }
    // END OF CREATE JWT TOKEN METHOD.

    public Boolean validateToken(String token, UserDetails userDetails){
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }
    // END OF VALIDATE TOKEN METHOD.

}
//End of JWT token service class.
