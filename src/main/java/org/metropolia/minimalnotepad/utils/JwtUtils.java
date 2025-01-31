package org.metropolia.minimalnotepad.utils;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;

public class JwtUtils {
    // nrMilliseconds * nrSeconds * nrMinutes * nrHours * nrDays
    private final int timeValidity = 1000 * 60 * 60 * 24;
    private final String secretKey = "TEST_KEY";

    public String generateToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + timeValidity))
                .signWith(SignatureAlgorithm.ES256,secretKey)
                .compact();
    }

}
