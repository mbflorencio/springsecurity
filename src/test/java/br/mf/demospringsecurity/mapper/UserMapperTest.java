package br.mf.demospringsecurity.mapper;

import br.mf.demospringsecurity.dto.UserDTO;
import br.mf.demospringsecurity.model.User;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserMapperTest {
    private final UserMapper mapper = new UserMapper();

    @Test
    void toDtoAndBack() {
        User user = new User();
        user.setId(1L);
        user.setName("John");
        user.setProfile("ADMIN");
        user.setLogin("john");
        user.setPassword("pass");

        UserDTO dto = mapper.toDto(user);
        assertNotNull(dto);
        assertEquals(user.getId(), dto.getId());
        assertEquals(user.getName(), dto.getName());
        assertEquals(user.getProfile(), dto.getProfile());
        assertEquals(user.getLogin(), dto.getLogin());
        assertEquals(user.getPassword(), dto.getPassword());

        User mapped = mapper.toEntity(dto);
        assertNotNull(mapped);
        assertEquals(dto.getId(), mapped.getId());
        assertEquals(dto.getName(), mapped.getName());
        assertEquals(dto.getProfile(), mapped.getProfile());
        assertEquals(dto.getLogin(), mapped.getLogin());
        assertEquals(dto.getPassword(), mapped.getPassword());
    }

    @Test
    void handlesNulls() {
        assertNull(mapper.toDto(null));
        assertNull(mapper.toEntity(null));
    }
}
