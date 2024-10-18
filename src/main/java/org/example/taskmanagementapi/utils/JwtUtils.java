package org.example.taskmanagementapi.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.example.taskmanagementapi.config.security.user.CustomUserDetails;
import org.example.taskmanagementapi.entities.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtUtils {

    @Value("${jwt.key}")
    private String SECRET_KEY;
    @Value("${jwt.expire}")
    private long EXPIRE_TIME;

    private String generateToken(Map<String,Object> extraClaims, User userDetails) {
        return Jwts
                .builder()
                .setClaims(extraClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRE_TIME))
                .signWith(getsinginKey(),SignatureAlgorithm.HS256)
                .compact();
    }

    private Key getsinginKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }


    private Claims extractClaims(String jwt) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getsinginKey())
                .build()
                .parseClaimsJws(jwt)
                .getBody();
    }

    public String generateToken(User userDetails) {
        Map<String,Object> data = new HashMap<>();
        data.put("id", userDetails.getId());
        data.put("email", userDetails.getEmail());
        return generateToken(data, userDetails);
    }
    public <T> T extractClaim(String jwt, Function<Claims,T> extractResolver) {
        Claims claims = extractClaims(jwt);
        return extractResolver.apply(claims);
    }

    public boolean isTokenExpired(String token) {
        Date expirationDate = extractClaim(token, Claims::getExpiration);
        return expirationDate.before(new Date());
    }
    public boolean isTokenValid(String token, CustomUserDetails userDetails) {
        String userEmail = extractClaim(token, Claims::getSubject);
        return userEmail.equals(userDetails.getUsername()) && !isTokenExpired(token);

    }

}
