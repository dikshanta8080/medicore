package com.acharya.dikshanta.HospitalManagement.common.service;

import com.acharya.dikshanta.HospitalManagement.common.config.AppData;
import com.acharya.dikshanta.HospitalManagement.identity.model.UserPrincipal;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.DecodingException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class JwtService {

    private final AppData appData;

    private SecretKey generateKey() {
        String secret = appData.getJwt().getSecret();
        byte[] bytes;

        try {
            bytes = Decoders.BASE64.decode(secret);
        } catch (DecodingException exception) {
            bytes = secret.getBytes(StandardCharsets.UTF_8);
        }

        return Keys.hmacShaKeyFor(bytes);
    }

    public String generateToken(UserPrincipal userPrincipal) {
        if (userPrincipal == null) {
            throw new IllegalArgumentException("UserPrincipal cannot be null");
        }

        JwtBuilder builder = Jwts.builder()
                .subject(userPrincipal.getUsername())
                .signWith(generateKey())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + appData.getJwt().getExpiry() * 1000 * 60L));

        if (userPrincipal.getId() != null) {
            builder.claim("id", userPrincipal.getId().toString());
        }
        if (userPrincipal.getEmail() != null) {
            builder.claim("email", userPrincipal.getEmail());
        }
        if (userPrincipal.getRole() != null) {
            builder.claim("role", userPrincipal.getRole().name());
        }
        if (userPrincipal.getStaffId() != null) {
            builder.claim("staffId", userPrincipal.getStaffId().toString());
        }

        return builder.compact();
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(generateKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private <T> T resolveClaim(String token, Function<Claims, T> claimsResolver) {
        return claimsResolver.apply(extractAllClaims(token));
    }

    public String extractUsername(String token) {
        return resolveClaim(token, Claims::getSubject);
    }

    public Date extractExpiry(String token) {
        return resolveClaim(token, Claims::getExpiration);
    }

    private boolean isTokenExpired(String token) {
        return extractExpiry(token).before(new Date());
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        return extractUsername(token).equals(userDetails.getUsername()) && !isTokenExpired(token);
    }
}
