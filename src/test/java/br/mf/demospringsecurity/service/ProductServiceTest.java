package br.mf.demospringsecurity.service;

import br.mf.demospringsecurity.model.Product;
import br.mf.demospringsecurity.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {
    @Mock
    private ProductRepository repository;
    @InjectMocks
    private ProductService service;

    @Test
    void saveDelegatesToRepository() {
        Product p = new Product();
        when(repository.save(p)).thenReturn(p);
        assertSame(p, service.save(p));
        verify(repository).save(p);
    }

    @Test
    void findAllReturnsList() {
        when(repository.findAll()).thenReturn(Collections.emptyList());
        assertTrue(service.findAll().isEmpty());
    }

    @Test
    void findByIdDelegates() {
        when(repository.findById(1L)).thenReturn(Optional.empty());
        assertTrue(service.findById(1L).isEmpty());
    }

    @Test
    void deleteByIdDelegates() {
        service.deleteById(4L);
        verify(repository).deleteById(4L);
    }
}
