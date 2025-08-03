package br.mf.demospringsecurity.service;

import br.mf.demospringsecurity.model.User;
import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.Base64;

@Service
public class TokenService {

    private static final String SECRET = "0123456789abcdef0123456789abcdef"; // 32 bytes -> 256 bit key
    private static final String ALGORITHM = "AES/GCM/NoPadding";

    private SecretKey getKey() {
        return new SecretKeySpec(SECRET.getBytes(StandardCharsets.UTF_8), "AES");
    }

    public String generateToken(User user) {
        String payload = user.getProfile() + ":" + LocalDateTime.now();
        byte[] iv = new byte[12];
        new SecureRandom().nextBytes(iv);
        try {
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, getKey(), new GCMParameterSpec(128, iv));
            byte[] encrypted = cipher.doFinal(payload.getBytes(StandardCharsets.UTF_8));
            ByteBuffer buffer = ByteBuffer.allocate(iv.length + encrypted.length);
            buffer.put(iv);
            buffer.put(encrypted);
            return Base64.getEncoder().encodeToString(buffer.array());
        } catch (Exception e) {
            throw new RuntimeException("Could not generate token", e);
        }
    }

    public TokenValidationResult validateToken(String token) {
        if (token == null) {
            return new TokenValidationResult(false, null, null);
        }
        try {
            byte[] decoded = Base64.getDecoder().decode(token);
            if (decoded.length < 12) {
                return new TokenValidationResult(false, null, null);
            }
            ByteBuffer buffer = ByteBuffer.wrap(decoded);
            byte[] iv = new byte[12];
            buffer.get(iv);
            byte[] cipherText = new byte[buffer.remaining()];
            buffer.get(cipherText);

            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, getKey(), new GCMParameterSpec(128, iv));
            byte[] decrypted = cipher.doFinal(cipherText);
            String payload = new String(decrypted, StandardCharsets.UTF_8);
            String[] parts = payload.split(":", 2);
            if (parts.length != 2) {
                return new TokenValidationResult(false, null, null);
            }
            LocalDateTime date = LocalDateTime.parse(parts[1]);
            return new TokenValidationResult(true, parts[0], date);
        } catch (DateTimeParseException ex) {
            return new TokenValidationResult(false, null, null);
        } catch (Exception ex) {
            return new TokenValidationResult(false, null, null);
        }
    }

    public static class TokenValidationResult {
        private final boolean valid;
        private final String profile;
        private final LocalDateTime date;

        public TokenValidationResult(boolean valid, String profile, LocalDateTime date) {
            this.valid = valid;
            this.profile = profile;
            this.date = date;
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
    }
}
