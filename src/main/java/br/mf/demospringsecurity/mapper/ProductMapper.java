package br.mf.demospringsecurity.mapper;

import br.mf.demospringsecurity.dto.ProductDTO;
import br.mf.demospringsecurity.model.Product;
import org.springframework.stereotype.Component;

@Component
public class ProductMapper {

    public ProductDTO toDto(Product entity) {
        if (entity == null) return null;
        ProductDTO dto = new ProductDTO();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setDescription(entity.getDescription());
        dto.setQuantity(entity.getQuantity());
        dto.setValue(entity.getValue());
        return dto;
    }

    public Product toEntity(ProductDTO dto) {
        if (dto == null) return null;
        Product entity = new Product();
        entity.setId(dto.getId());
        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());
        entity.setQuantity(dto.getQuantity());
        entity.setValue(dto.getValue());
        return entity;
    }
}
