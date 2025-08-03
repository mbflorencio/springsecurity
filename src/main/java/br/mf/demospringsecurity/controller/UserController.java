package br.mf.demospringsecurity.controller;

import br.mf.demospringsecurity.dto.UserDTO;
import br.mf.demospringsecurity.mapper.UserMapper;
import br.mf.demospringsecurity.model.User;
import br.mf.demospringsecurity.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService service;
    private final UserMapper mapper;

    @Autowired
    public UserController(UserService service, UserMapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    @Operation(summary = "List all users")
    @GetMapping
    public List<UserDTO> list() {
        return service.findAll().stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    @Operation(summary = "Get user by ID")
    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getById(@PathVariable Long id) {
        Optional<User> user = service.findById(id);
        return user.map(u -> ResponseEntity.ok(mapper.toDto(u)))
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Create new user")
    @PostMapping
    public ResponseEntity<UserDTO> create(@RequestBody UserDTO dto) {
        User saved = service.save(mapper.toEntity(dto));
        return ResponseEntity.created(URI.create("/users/" + saved.getId()))
                .body(mapper.toDto(saved));
    }



    @Operation(summary = "Update user")
    @PutMapping("/{id}")
    public ResponseEntity<UserDTO> update(@PathVariable Long id, @RequestBody UserDTO dto) {
        Optional<User> existing = service.findById(id);
        if (existing.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        User entity = mapper.toEntity(dto);
        entity.setId(id);
        User updated = service.save(entity);
        return ResponseEntity.ok(mapper.toDto(updated));
    }

    @Operation(summary = "Delete user")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
