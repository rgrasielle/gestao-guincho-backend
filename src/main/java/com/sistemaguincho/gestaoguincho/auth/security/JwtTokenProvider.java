package com.sistemaguincho.gestaoguincho.auth.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.util.Date;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.beans.factory.annotation.Value;

import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

@Component
public class JwtTokenProvider {

    @Value("${security.jwt.secret}")
    private String jwtSecret;

    @Value("${security.jwt.expiration-ms:86400000}") // 24h por padrão
    private long jwtExpirationMs;

    private SecretKey getSigningKey() {
        // Keys.hmacShaKeyFor já retorna uma SecretKey, então está tudo bem.
        return Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
    }

     // Gera um token JWT usando a API moderna da biblioteca jjwt.
    public String generateToken(UserDetails user) {
        Date now = new Date();
        Date expiry = new Date(now.getTime() + jwtExpirationMs);

        SecretKey key = getSigningKey();

        // API MODERNA: usa métodos diretos como subject(), issuedAt(), etc.
        return Jwts.builder()
                .subject(user.getUsername()) // Substitui setSubject()
                .issuedAt(now)               // Substitui setIssuedAt()
                .expiration(expiry)          // Substitui setExpiration()
                .signWith(key)               // O signWith permanece o mesmo
                .compact();
    }

    // Extrai o nome de usuário do token.
    public String getUsernameFromToken(String token) {
        return parseClaims(token).getSubject();
    }


     // Valida o token, verificando o nome de usuário e a data de expiração.
    public boolean validateToken(String token, UserDetails user) {
        String username = getUsernameFromToken(token);
        return username.equals(user.getUsername()) && !isTokenExpired(token);
    }

    // Verifica se o token expirou.
    private boolean isTokenExpired(String token) {
        return parseClaims(token).getExpiration().before(new Date());
    }

    // Analisa o token para extrair as 'claims' (informações) usando a API moderna.
    private Claims parseClaims(String token) {
        // API MODERNA: Jwts.parser() substitui Jwts.parserBuilder()
        return Jwts.parser()
                .verifyWith(getSigningKey()) // Novo método para verificar a chave
                .build()
                .parseSignedClaims(token) // Substitui parseClaimsJws()
                .getPayload(); // Substitui getBody()
    }
}