package br.mf.demospringsecurity.mapper;

import br.mf.demospringsecurity.dto.RequestItemDTO;
import br.mf.demospringsecurity.model.Product;
import br.mf.demospringsecurity.model.RequestProduct;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RequestItemMapperTest {
    private final RequestItemMapper mapper = new RequestItemMapper();

    @Test
    void toDtoAndBack() {
        Product product = new Product();
        product.setId(10L);
        RequestProduct item = new RequestProduct();
        item.setProduct(product);
        item.setQuantity(3);

        RequestItemDTO dto = mapper.toDto(item);
        assertNotNull(dto);
        assertEquals(product.getId(), dto.getProductId());
        assertEquals(item.getQuantity(), dto.getQuantity());

        RequestProduct mapped = mapper.toEntity(dto);
        assertNotNull(mapped);
        assertEquals(dto.getProductId(), mapped.getProduct().getId());
        assertEquals(dto.getQuantity(), mapped.getQuantity());
    }

    @Test
    void handlesNulls() {
        assertNull(mapper.toDto(null));
        assertNull(mapper.toEntity(null));
    }
}
