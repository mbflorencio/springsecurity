package br.mf.demospringsecurity.controller;

import br.mf.demospringsecurity.dto.RequestDTO;
import br.mf.demospringsecurity.mapper.RequestMapper;
import br.mf.demospringsecurity.model.Request;
import br.mf.demospringsecurity.model.RequestProduct;
import br.mf.demospringsecurity.service.RequestService;
import br.mf.demospringsecurity.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/requests")
public class RequestController {

    private final RequestService service;
    private final UserService userService;
    private final RequestMapper mapper;

    @Autowired
    public RequestController(RequestService service, UserService userService, RequestMapper mapper) {
        this.service = service;
        this.userService = userService;
        this.mapper = mapper;
    }

    @Operation(summary = "List all requests")
    @GetMapping
    public List<RequestDTO> list(@RequestParam(value = "userId", required = false) Long userId) {
        List<Request> requests = (userId != null) ?
                service.findByUserId(userId) :
                service.findAll();
        return requests.stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    @Operation(summary = "Get request by ID")
    @GetMapping("/{id}")
    public ResponseEntity<RequestDTO> getById(@PathVariable Long id) {
        Optional<Request> req = service.findById(id);
        return req.map(r -> ResponseEntity.ok(mapper.toDto(r)))
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Create new request")
    @PostMapping
    public ResponseEntity<RequestDTO> create(@RequestBody RequestDTO dto) {
        Request entity = mapper.toEntity(dto);
        List<RequestProduct> itens = entity.getItems();
        if (dto.getUserId() != null) {
            userService.findById(dto.getUserId()).ifPresent(entity::setUser);
        }

        Request saved = service.save(entity);
        Request teste = new Request();
        teste.setId(saved.getId());
        itens.forEach(item -> item.setRequest(teste));
        saved.setItems( service.saveItens(itens));
        return ResponseEntity.created(URI.create("/requests/" + saved.getId()))
                .body(mapper.toDto(saved));
    }

    @Operation(summary = "Update request")
    @PutMapping("/{id}")
    public ResponseEntity<RequestDTO> update(@PathVariable Long id, @RequestBody RequestDTO dto) {
        Optional<Request> existing = service.findById(id);
        if (existing.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Request entity = mapper.toEntity(dto);
        entity.setId(id);
        if (dto.getUserId() != null) {
            userService.findById(dto.getUserId()).ifPresent(entity::setUser);
        }
        Request updated = service.save(entity);
        return ResponseEntity.ok(mapper.toDto(updated));
    }

    @Operation(summary = "Delete request")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
