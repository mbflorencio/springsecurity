package br.mf.demospringsecurity.service;

import br.mf.demospringsecurity.model.User;
import br.mf.demospringsecurity.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.junit.jupiter.api.BeforeEach;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @Mock
    private UserRepository repository;
    @Mock
    private PasswordEncoder passwordEncoder;
    private UserService service;

    @BeforeEach
    void setup() {
        service = new UserService(repository, passwordEncoder);
    }

    @Test
    void saveEncodesPassword() {
        User u = new User();
        u.setPassword("secret");
        when(passwordEncoder.encode("secret")).thenReturn("hashed");
        when(repository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        User result = service.save(u);

        assertEquals("hashed", result.getPassword());
        verify(passwordEncoder).encode("secret");
        verify(repository).save(any(User.class));
    }

    @Test
    void findAllReturnsList() {
        List<User> users = Arrays.asList(new User(), new User());
        when(repository.findAll()).thenReturn(users);
        assertEquals(users, service.findAll());
    }

    @Test
    void findByIdDelegates() {
        User u = new User();
        when(repository.findById(1L)).thenReturn(Optional.of(u));
        assertTrue(service.findById(1L).isPresent());
    }

    @Test
    void deleteByIdDelegates() {
        service.deleteById(3L);
        verify(repository).deleteById(3L);
    }

    @Test
    void loginReturnsUserWhenPasswordMatches() {
        User user = new User();
        user.setPassword("hash");
        when(repository.findByLogin("john")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("secret", "hash")).thenReturn(true);

        Optional<User> result = service.login("john", "secret");

        assertTrue(result.isPresent());
        assertSame(user, result.get());
    }

    @Test
    void loginFailsWhenInvalid() {
        when(repository.findByLogin("john")).thenReturn(Optional.empty());

        Optional<User> result = service.login("john", "bad");

        assertTrue(result.isEmpty());
    }
}
