package br.mf.demospringsecurity.mapper;

import br.mf.demospringsecurity.dto.UserDTO;
import br.mf.demospringsecurity.model.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    public UserDTO toDto(User entity) {
        if (entity == null) return null;
        UserDTO dto = new UserDTO();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setProfile(entity.getProfile());
        dto.setLogin(entity.getLogin());
        dto.setPassword(entity.getPassword());
        return dto;
    }

    public User toEntity(UserDTO dto) {
        if (dto == null) return null;
        User entity = new User();
        entity.setId(dto.getId());
        entity.setName(dto.getName());
        entity.setProfile(dto.getProfile());
        entity.setLogin(dto.getLogin());
        entity.setPassword(dto.getPassword());
        return entity;
    }
}
