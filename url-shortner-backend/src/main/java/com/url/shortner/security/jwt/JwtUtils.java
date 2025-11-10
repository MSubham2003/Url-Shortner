package com.url.shortner.security.jwt;


import com.url.shortner.service.UserDetailsImpl;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.GrantedAuthority;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Date;
import java.util.stream.Collectors;


// Authorization -> Bearer <TOKEN>
@Configuration
public class JwtUtils {
    @Value("${jwt.secret}")
    private String jwtSecret;
    @Value("${jwt.expiration.ms}")
    private int jwtExpirationMs;

    public String getJwtFromReqHeader(HttpServletRequest request) {
        String receivedBearerToken = request.getHeader("Authorization");
        if (receivedBearerToken != null && receivedBearerToken.startsWith("Bearer ")) {
            // return the actual "<TOKEN>" instead of complete "Bearer <TOKEN>"
            return receivedBearerToken.substring(7);
        }
        return null;
    }

    // UserDetailsImpl is used here so that i can inject roles in the jwt token
    public String generateJwtToken(UserDetailsImpl userDetails) {
        String username = userDetails.getUsername();
        // will get roles in comma separated value like(user,admin)
        // String roles = userDetails.getAuthorities().stream().map(authority -> authority.getAuthority()).collect(Collectors.joining(","));
        String roles = userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.joining(","));
        return Jwts.builder().subject(username).claim("roles", roles).issuedAt(new Date()).expiration(new Date(new Date().getTime() + jwtExpirationMs)).signWith(getKey()).compact();
    }

    public String getUserNameFromJwtToken(String token) {
        return Jwts.parser().verifyWith((SecretKey) getKey()).build().parseSignedClaims(token).getPayload().getSubject();
    }

    private Key getKey() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
    }

    public boolean validateJwtToken(String authToken) {
        try {
            Jwts.parser().verifyWith((SecretKey) getKey()).build().parseSignedClaims(authToken);
            return true;
        } catch (JwtException e) {
            throw new RuntimeException(e);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(e);
        }
    }
}
