package br.mf.demospringsecurity.service;

import br.mf.demospringsecurity.model.User;
import br.mf.demospringsecurity.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository repository;
    private final PasswordEncoder encoder;

    @Autowired
    public UserService(UserRepository repository) {
        this.repository = repository;
        this.encoder = new BCryptPasswordEncoder();

    }

    public User save(User user) {
        user.setPassword( encoder.encode( user.getPassword()));
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
            if (encoder.matches(rawPassword, user.getPassword())) {
                return Optional.of(user);
            }
        }
        return Optional.empty();
    }
}
