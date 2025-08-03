package br.mf.demospringsecurity.mapper;

import br.mf.demospringsecurity.dto.RequestItemDTO;
import br.mf.demospringsecurity.model.Product;
import br.mf.demospringsecurity.model.RequestProduct;
import org.springframework.stereotype.Component;

@Component
public class RequestItemMapper {
    public RequestItemDTO toDto(RequestProduct entity) {
        if (entity == null) return null;
        RequestItemDTO dto = new RequestItemDTO();
        if (entity.getProduct() != null) {
            dto.setProductId(entity.getProduct().getId());
        }
        dto.setQuantity(entity.getQuantity());
        return dto;
    }

    public RequestProduct toEntity(RequestItemDTO dto) {
        if (dto == null) return null;
        RequestProduct entity = new RequestProduct();
        entity.setQuantity(dto.getQuantity());
        if (dto.getProductId() != null) {
            Product product = new Product();
            product.setId(dto.getProductId());
            entity.setProduct(product);
        }
        return entity;
    }
}
