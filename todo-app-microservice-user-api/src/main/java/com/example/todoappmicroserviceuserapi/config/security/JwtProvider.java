package com.example.todoappmicroserviceuserapi.config.security;


import com.example.todoappmicroserviceuserapi.model.AuthUser;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.NonNull;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component

public class JwtProvider {

    @Value("${jwt.secret.key}")
    private String secretKey;
    @Value("${jwt.expiry.in.minutes}")
    private long expiryInMinutes;
    @Value("${jwt.refreshToken.expiry.in.hours}")
    private long refreshTokenDurationHour;

    public String extractUsername(@NonNull String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public String generateToken(AuthUser authUser) {
        return generateToken(new HashMap<>(), authUser, true);
    }


    public String generateToken(Map<String, Object> extraClaims, AuthUser authUser, boolean isRefreshToken) {
        return getToken(extraClaims,authUser.getUsername(),expiryInMinutes*1000*60*60*60*60*60*60*60*60); //todo after development will edited
    }

    private String getToken(Map<String, Object> extraClaims, String subject , long duration) {
        return Jwts.builder().
                setClaims(extraClaims).
                setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + duration))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private Claims extractAllClaims(String token) {
        JwtParser jwtParser = Jwts
                .parserBuilder()
                .setSigningKey(getSignInKey())
                .build();
        Jws<Claims> claimsJws = jwtParser
                .parseClaimsJws(token);
        Claims body = claimsJws.getBody();
        return body;
    }

    private Key getSignInKey() {
        Base64.Decoder decoder = Base64.getDecoder();
        byte[] keyBytes = decoder.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }


}
