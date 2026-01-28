package ru.stroy1click.web.common.service.impl;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.stroy1click.web.common.service.JwtService;
import ru.stroy1click.web.user.dto.UserDto;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class JwtServiceImpl implements JwtService {

    @Value(value = "${jwt.secret}")
    public String SECRET;

    @Override
    public String generate() {
        Map<String, Object> claims = new HashMap<>();
        claims.put("role", "ROLE_ADMIN");
        claims.put("emailConfirmed", "true");
        return createToken(claims);
    }

    private String createToken(Map<String, Object> claims) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject("admin@gmail.com")
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 300))
                .signWith(getSignKey(), SignatureAlgorithm.HS256).compact();
    }
    private Key getSignKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
