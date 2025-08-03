package br.mf.demospringsecurity.controller;

import br.mf.demospringsecurity.dto.AuthDto;
import br.mf.demospringsecurity.model.User;
import br.mf.demospringsecurity.service.UserService;
import br.mf.demospringsecurity.service.TokenService;
import br.mf.demospringsecurity.service.TokenService.TokenValidationResult;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserService service;
    private final TokenService tokenService;

    @Autowired
    public AuthController(UserService service, TokenService tokenService) {
        this.service = service;
        this.tokenService = tokenService;
    }

    @Operation(summary = "Login user")
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody AuthDto dto) {
        System.out.println(dto.toString());
        Optional<User> user = service.login(dto.getLogin(), dto.getPassword());
        if (user.isPresent()) {
            String token = tokenService.generateToken(user.get());
            return ResponseEntity.ok(token);
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @Operation(summary = "Validate token")
    @PostMapping("/validate")
    public ResponseEntity<TokenValidationResult> validate(@RequestBody String token) {
        TokenValidationResult result = tokenService.validateToken(token);
        if (result.isValid()) {
            return ResponseEntity.ok(result);
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(result);
    }
}
