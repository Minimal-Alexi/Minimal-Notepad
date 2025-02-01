package org.metropolia.minimalnotepad.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.metropolia.minimalnotepad.model.User;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.function.Function;

@Component
public class JwtUtils {
    // nrMilliseconds * nrSeconds * nrMinutes * nrHours * nrDays
    private final int timeValidity = 1000 * 60 * 60 * 24;
    private final String secretKey = "d6e5aea775fad8923f14755ac497bf51ca4e06a576be3a7a1d7583c888170cf0c59db49baed7e6257c8d54e54b0b2a05a74e961462f570b7028b98da327021fe96bc7cba96f57e4dd7ee3ef81efaeb2954bc495fd28b7d60f18d4c75bbd1e1ad7534989d9760d0bde5e8ccb45915f4efac29f70cc11e39c52ee9149bf8eb4e20e8c3de1a65e952ef117e75c500ce8b5b64959e0cc29d35796a9d22dfd63ed2dab2f2cd69be9f11126d1dbc42f2194df2b9db49997a0d35ba64f4c72dda5102ae536861e4e0635f8e1236c6d2dee204125f189703e1c3bbeab77e87f2a6cb656e7ac531b4933653c74b142ee9b52b5e7e41c27d5177555ba3f72f88c8298d2ec3";
    

    public String generateToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + timeValidity))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    private Date extractExpirationDate(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public boolean isTokenExpired(String token) {
        return extractExpirationDate(token).before(new Date());
    }

    public boolean validateToken(String token, User user) {
        return (user.getUsername().equals(extractUsername(token)) && !isTokenExpired(token));
    }

}
