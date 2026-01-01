package com.mypensamiento.mypensamiento.infrastructure.security;

import com.mypensamiento.mypensamiento.domain.model.User;
import com.mypensamiento.mypensamiento.infrastructure.dto.TokenResponse;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class JwtService {

    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${app.security.jwt.access-expiration}")
    private Duration accessExpiration;

    @Value("${app.security.jwt.refresh-expiration}")
    private Duration refreshExpiration;

    public TokenResponse generateToken(UserDetails userDetails, LocalDateTime transactionTime) {

        Map<String, Object> claims = new HashMap<>();

        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList();

        claims.put("roles", roles);

        return buildToken(
                claims,
                userDetails,
                transactionTime,
                accessExpiration.toMillis()
        );
    }

    public TokenResponse generateRefreshToken(UserDetails userDetails, LocalDateTime transactionTime) {

        Map<String, Object> claims = new HashMap<>();

        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList();

        claims.put("roles", roles);

        return buildToken(
                claims,
                 userDetails,
                 transactionTime,
                 refreshExpiration.toMillis()
         );
     }

     private TokenResponse buildToken(
             Map<String,Object> claim,
             UserDetails userDetails,
             LocalDateTime transactionTime,
             Long expirationTime
     ) {

        LocalDateTime expirationDate = transactionTime.plus(expirationTime, ChronoUnit.MILLIS);

         Date iatDate = Date.from(transactionTime.atZone(ZoneId.systemDefault()).toInstant());
         Date expDate = Date.from(expirationDate.atZone(ZoneId.systemDefault()).toInstant());

        String token = Jwts
                    .builder()
                    .setClaims(claim)
                    .setSubject(userDetails.getUsername())
                    .setIssuedAt(iatDate)
                    .setExpiration(expDate)
                    .signWith(getSignIngKey(), SignatureAlgorithm.HS256)
                    .compact();
        return new TokenResponse(token, expirationDate);
     }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    private  <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignIngKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
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

    private Key getSignIngKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

}
