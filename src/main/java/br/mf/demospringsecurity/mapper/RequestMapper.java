package br.mf.demospringsecurity.mapper;

import br.mf.demospringsecurity.dto.RequestDTO;
import br.mf.demospringsecurity.model.Request;
import br.mf.demospringsecurity.model.User;
import br.mf.demospringsecurity.model.RequestProduct;
import br.mf.demospringsecurity.dto.RequestItemDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.stream.Collectors;


@Component
public class RequestMapper {

    private final RequestItemMapper requestItemMapper;

    @Autowired
    public RequestMapper(RequestItemMapper requestItemMapper) {
        this.requestItemMapper = requestItemMapper;
    }

    public RequestDTO toDto(Request entity) {
        if (entity == null) return null;
        RequestDTO dto = new RequestDTO();
        dto.setId(entity.getId());
        dto.setRequestText(entity.getRequestText());
        dto.setRequestDate(entity.getRequestDate());
        dto.setResponseText(entity.getResponseText());
        if (entity.getUser() != null) {
            dto.setUserId(entity.getUser().getId());
        }
        if (entity.getItems() != null) {
            dto.setItems(entity.getItems().stream()
                    .map(requestItemMapper::toDto)
                    .collect(Collectors.toList()));
        }
        return dto;
    }

    public Request toEntity(RequestDTO dto) {
        if (dto == null) return null;
        Request entity = new Request();
        entity.setId(dto.getId());
        entity.setRequestText(dto.getRequestText());
        entity.setRequestDate(dto.getRequestDate());
        entity.setResponseText(dto.getResponseText());
        if (dto.getUserId() != null) {
            User user = new User();
            user.setId(dto.getUserId());
            entity.setUser(user);
        }
        if (dto.getItems() != null) {
            List<RequestProduct> items = dto.getItems().stream()
                    .map(requestItemMapper::toEntity)
                    .collect(Collectors.toList());
            items.forEach(i -> i.setRequest(entity));
            entity.setItems(items);
        }
        return entity;
    }
}
