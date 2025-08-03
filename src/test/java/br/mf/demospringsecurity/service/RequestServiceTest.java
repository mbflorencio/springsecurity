package br.mf.demospringsecurity.service;

import br.mf.demospringsecurity.model.Request;
import br.mf.demospringsecurity.repository.RequestRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RequestServiceTest {
    @Mock
    private RequestRepository repository;
    @InjectMocks
    private RequestService service;

    @Test
    void saveDelegatesToRepository() {
        Request r = new Request();
        when(repository.save(r)).thenReturn(r);
        assertSame(r, service.save(r));
        verify(repository).save(r);
    }

    @Test
    void findAllReturnsList() {
        when(repository.findAll()).thenReturn(Collections.emptyList());
        assertTrue(service.findAll().isEmpty());
    }

    @Test
    void findByUserIdDelegates() {
        when(repository.findByUserId(2L)).thenReturn(Arrays.asList(new Request()));
        assertEquals(1, service.findByUserId(2L).size());
    }

    @Test
    void findByIdDelegates() {
        when(repository.findById(1L)).thenReturn(Optional.empty());
        assertTrue(service.findById(1L).isEmpty());
    }

    @Test
    void deleteByIdDelegates() {
        service.deleteById(5L);
        verify(repository).deleteById(5L);
    }
}
