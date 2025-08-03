package br.mf.demospringsecurity.mapper;

import br.mf.demospringsecurity.dto.ProductDTO;
import br.mf.demospringsecurity.model.Product;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class ProductMapperTest {
    private final ProductMapper mapper = new ProductMapper();

    @Test
    void toDtoAndBack() {
        Product p = new Product();
        p.setId(2L);
        p.setName("prod");
        p.setDescription("desc");
        p.setQuantity(5);
        p.setValue(new BigDecimal("9.99"));

        ProductDTO dto = mapper.toDto(p);
        assertNotNull(dto);
        assertEquals(p.getId(), dto.getId());
        assertEquals(p.getName(), dto.getName());
        assertEquals(p.getDescription(), dto.getDescription());
        assertEquals(p.getQuantity(), dto.getQuantity());
        assertEquals(p.getValue(), dto.getValue());

        Product mapped = mapper.toEntity(dto);
        assertNotNull(mapped);
        assertEquals(dto.getId(), mapped.getId());
        assertEquals(dto.getName(), mapped.getName());
        assertEquals(dto.getDescription(), mapped.getDescription());
        assertEquals(dto.getQuantity(), mapped.getQuantity());
        assertEquals(dto.getValue(), mapped.getValue());
    }

    @Test
    void handlesNulls() {
        assertNull(mapper.toDto(null));
        assertNull(mapper.toEntity(null));
    }
}
