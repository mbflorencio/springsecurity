package br.mf.demospringsecurity.mapper;

import br.mf.demospringsecurity.dto.RequestDTO;
import br.mf.demospringsecurity.model.Product;
import br.mf.demospringsecurity.model.Request;
import br.mf.demospringsecurity.model.RequestProduct;
import br.mf.demospringsecurity.model.User;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

class RequestMapperTest {
    private final RequestItemMapper itemMapper = new RequestItemMapper();
    private final RequestMapper mapper = new RequestMapper(itemMapper);

    @Test
    void toDtoAndBack() {
        User user = new User();
        user.setId(5L);
        Product product = new Product();
        product.setId(7L);
        RequestProduct item = new RequestProduct();
        item.setProduct(product);
        item.setQuantity(2);
        Request request = new Request();
        request.setId(1L);
        request.setRequestText("text");
        request.setRequestDate(LocalDateTime.now());
        request.setResponseText("resp");
        request.setUser(user);
        request.setItems(Collections.singletonList(item));
        item.setRequest(request);

        RequestDTO dto = mapper.toDto(request);
        assertNotNull(dto);
        assertEquals(request.getId(), dto.getId());
        assertEquals(request.getRequestText(), dto.getRequestText());
        assertEquals(request.getResponseText(), dto.getResponseText());
        assertEquals(request.getUser().getId(), dto.getUserId());
        assertEquals(1, dto.getItems().size());
        assertEquals(product.getId(), dto.getItems().get(0).getProductId());

        Request entity = mapper.toEntity(dto);
        assertNotNull(entity);
        assertEquals(dto.getId(), entity.getId());
        assertEquals(dto.getRequestText(), entity.getRequestText());
        assertEquals(dto.getResponseText(), entity.getResponseText());
        assertEquals(dto.getUserId(), entity.getUser().getId());
        assertEquals(dto.getItems().size(), entity.getItems().size());
        assertEquals(dto.getItems().get(0).getProductId(), entity.getItems().get(0).getProduct().getId());
    }

    @Test
    void handlesNulls() {
        assertNull(mapper.toDto(null));
        assertNull(mapper.toEntity(null));
    }
}
