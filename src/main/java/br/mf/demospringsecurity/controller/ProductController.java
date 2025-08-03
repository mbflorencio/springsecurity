package br.mf.demospringsecurity.controller;

import br.mf.demospringsecurity.dto.ProductDTO;
import br.mf.demospringsecurity.mapper.ProductMapper;
import br.mf.demospringsecurity.model.Product;
import br.mf.demospringsecurity.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/products")
public class ProductController {

    private final ProductService service;
    private final ProductMapper mapper;

    @Autowired
    public ProductController(ProductService service, ProductMapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    @Operation(summary = "List all products")
    @GetMapping
    public List<ProductDTO> list() {
        List<Product> products = service.findAll();
        return products.stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    @Operation(summary = "Get product by ID")
    @GetMapping("/{id}")
    public ResponseEntity<ProductDTO> getById(@PathVariable Long id) {
        Optional<Product> product = service.findById(id);
        return product.map(p -> ResponseEntity.ok(mapper.toDto(p)))
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Create new product")
    @PostMapping
    public ResponseEntity<ProductDTO> create(@RequestBody ProductDTO dto) {
        Product entity = mapper.toEntity(dto);
        Product saved = service.save(entity);
        return ResponseEntity.created(URI.create("/products/" + saved.getId()))
                .body(mapper.toDto(saved));
    }

    @Operation(summary = "Update product")
    @PutMapping("/{id}")
    public ResponseEntity<ProductDTO> update(@PathVariable Long id, @RequestBody ProductDTO dto) {
        Optional<Product> existing = service.findById(id);
        if (existing.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Product entity = mapper.toEntity(dto);
        entity.setId(id);
        Product updated = service.save(entity);
        return ResponseEntity.ok(mapper.toDto(updated));
    }

    @Operation(summary = "Delete product")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
