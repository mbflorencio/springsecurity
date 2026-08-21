package br.mf.demospringsecurity.service;

import br.mf.demospringsecurity.model.User;
import br.mf.demospringsecurity.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository repository, PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
    }

    public User save(User user) {
        if (user == null) {
            throw new IllegalArgumentException("user must not be null");
        }

        if (user.getId() != null) {
            Optional<User> existingOpt = repository.findById(user.getId());
            if (existingOpt.isPresent()) {
                User existing = existingOpt.get();

                if (user.getLogin() != null && !user.getLogin().equals(existing.getLogin())) {
                    if (repository.existsByLogin(user.getLogin())) {
                        throw new ResponseStatusException(HttpStatus.CONFLICT, "Login already exists: " + user.getLogin());
                    }
                }

                String incomingPassword = user.getPassword();
                if (incomingPassword == null || incomingPassword.isBlank()) {
                    user.setPassword(existing.getPassword());
                } else if (looksEncoded(incomingPassword)) {
                    user.setPassword(incomingPassword);
                } else {
                    user.setPassword(passwordEncoder.encode(incomingPassword));
                }
                return repository.save(user);
            }
        }

        if (user.getLogin() != null && repository.existsByLogin(user.getLogin())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Login already exists: " + user.getLogin());
        }

        if (user.getPassword() != null && !user.getPassword().isBlank() && !looksEncoded(user.getPassword())) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        return repository.save(user);
    }

    public List<User> findAll() {
        return repository.findAll();
    }

    public Optional<User> findById(Long id) {
        return repository.findById(id);
    }

    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    public Optional<User> login(String login, String rawPassword) {
        Optional<User> userOpt = repository.findByLogin(login);
        if (userOpt.isPresent()) {
            User user = userOpt.get();

            if (passwordEncoder.matches(rawPassword, user.getPassword())) {
                return Optional.of(user);
            }
        }
        return Optional.empty();
    }

    private boolean looksEncoded(String password) {
        return password.startsWith("$2a$") || password.startsWith("$2b$") || password.startsWith("$2y$");
    }
}
