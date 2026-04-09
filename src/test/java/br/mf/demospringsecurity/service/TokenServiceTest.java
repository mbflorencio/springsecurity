package br.mf.demospringsecurity.service;

import br.mf.demospringsecurity.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class TokenServiceTest {

    @Autowired
    private TokenService service;

    @Test
    void generateTokenEncryptsPayload() {
        User user = new User();
        user.setProfile("ADMIN");
        user.setLogin("admin");
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
        user.setLogin("user");
        String token = service.generateToken(user);
        TokenService.TokenValidationResult result = service.validateToken(token);
        assertTrue(result.isValid());
        assertEquals("USER", result.getProfile());
        assertEquals("user", result.getLogin());
        assertNotNull(result.getDate());
    }

    @Test
    void validateTokenFailsForBadFormat() {
        TokenService.TokenValidationResult result = service.validateToken("bad");
        assertFalse(result.isValid());
    }
}
