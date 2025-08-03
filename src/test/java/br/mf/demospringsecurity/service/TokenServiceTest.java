package br.mf.demospringsecurity.service;

import br.mf.demospringsecurity.model.User;
import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.*;

class TokenServiceTest {

    private final TokenService service = new TokenService();

    @Test
    void generateTokenEncryptsPayload() {
        User user = new User();
        user.setProfile("ADMIN");
        String token = service.generateToken(user);
        assertFalse(token.contains("ADMIN"));
        TokenService.TokenValidationResult result = service.validateToken(token);
        assertTrue(result.isValid());
        assertEquals("ADMIN", result.getProfile());
    }

    @Test
    void validateTokenParsesValues() {
        User user = new User();
        user.setProfile("USER");
        String token = service.generateToken(user);
        TokenService.TokenValidationResult result = service.validateToken(token);
        assertTrue(result.isValid());
        assertEquals("USER", result.getProfile());
        assertNotNull(result.getDate());
    }

    @Test
    void validateTokenFailsForBadFormat() {
        TokenService.TokenValidationResult result = service.validateToken("bad");
        assertFalse(result.isValid());
    }
}
