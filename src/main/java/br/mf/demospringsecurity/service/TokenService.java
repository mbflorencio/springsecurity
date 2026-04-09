package br.mf.demospringsecurity.service;

import br.mf.demospringsecurity.model.User;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

/**
 * Token expira em 6 h para equilibrar segurança e usabilidade.
 */
@Service
public class TokenService {

    private static final String CLAIM_PROFILE = "profile";

    private final Key key;
    private final long expiration;

    @Autowired
    public TokenService(@Value("${jwt.secret}") String secret,
                        @Value("${jwt.expiration:21600000}") long expiration) {
        this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        this.expiration = expiration;
    }



    public String generateToken(User user) {
        long now = System.currentTimeMillis();
        return Jwts.builder()
                .setSubject(user.getLogin())
                .claim(CLAIM_PROFILE, user.getProfile())
                .setIssuedAt(new Date(now))
                .setExpiration(new Date(now + expiration))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public TokenValidationResult validateToken(String token) {
        if (token == null) {
            return new TokenValidationResult(false, null, null);
        }
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            LocalDateTime issuedAt = LocalDateTime.ofInstant(claims.getIssuedAt().toInstant(), ZoneId.systemDefault());
            String login = claims.getSubject();
            String profile = claims.get(CLAIM_PROFILE, String.class);
            return new TokenValidationResult(true, profile, issuedAt, login);
        } catch (JwtException | IllegalArgumentException ex) {
            return new TokenValidationResult(false, null, null);
        }
    }

    public static class TokenValidationResult {
        private final boolean valid;
        private final String profile;
        private final LocalDateTime date;
        private final String login;

        public TokenValidationResult(boolean valid, String profile, LocalDateTime date) {
            this.valid = valid;
            this.profile = profile;
            this.date = date;
            this.login = null;
        }

        public TokenValidationResult(boolean valid, String profile, LocalDateTime date, String login) {
            this.valid = valid;
            this.profile = profile;
            this.date = date;
            this.login = login;
        }

        public boolean isValid() {
            return valid;
        }

        public String getProfile() {
            return profile;
        }

        public LocalDateTime getDate() {
            return date;
        }

        public String getLogin() {
            return login;
        }
    }
}
