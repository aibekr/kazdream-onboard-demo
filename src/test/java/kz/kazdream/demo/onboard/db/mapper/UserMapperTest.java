package kz.kazdream.demo.onboard.db.mapper;

import kz.kazdream.demo.onboard.db.dto.UserDto;
import kz.kazdream.demo.onboard.db.entity.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {ModelMapper.class, UserMapper.class})
class UserMapperTest {

    @Autowired
    private UserMapper mapper;

    @Test
    void entity_to_dto() {
        User entity = new User();
        entity.setId(1);
        entity.setLastName("LastName");
        entity.setFirstName("FirstName");
        entity.setEmail("email");
        entity.setLogin("login");

        UserDto dto = mapper.toDto(entity);

        Assertions.assertNotNull(dto);
        assertEquals(entity.getId(), dto.getId());
        assertEquals(entity.getEmail(), dto.getEmail());
        assertEquals(entity.getFirstName(), dto.getFirstName());
        assertEquals(entity.getLastName(), dto.getLastName());
        assertEquals(entity.getLogin(), dto.getLogin());
    }

    @Test
    void dto_to_entity() {

        UserDto dto = new UserDto();
        dto.setId(1);
        dto.setLastName("LastName");
        dto.setFirstName("FirstName");
        dto.setEmail("email");
        dto.setLogin("login");

        User entity = mapper.toEntity(dto);

        Assertions.assertNotNull(dto);
        assertEquals(dto.getId(), entity.getId());
        assertEquals(dto.getEmail(), entity.getEmail());
        assertEquals(dto.getFirstName(), entity.getFirstName());
        assertEquals(dto.getLastName(), entity.getLastName());
        assertEquals(dto.getLogin(), entity.getLogin());
    }

    @Test
    void entity_list_to_dto_list() {
        List<User> listEntity = new ArrayList<>();
        for (int i = 1; i < 10; i++) {
            User entity = new User();
            entity.setId(i);
            listEntity.add(entity);
        }

        List<UserDto> userDtos = mapper.toDtoList(listEntity);

        for (int i = 1; i < 10; i++) {
            assertEquals(listEntity.get(i - 1).getId(), userDtos.get(i - 1).getId());
        }
    }

    @Test
    void dto_list_to_entity_list() {
        List<UserDto> dtos = new ArrayList<>();
        for (int i = 1; i < 10; i++) {
            UserDto entity = new UserDto();
            entity.setId(i);
            dtos.add(entity);
        }

        List<User> entityList = mapper.toEntityList(dtos);

        for (int i = 1; i < 10; i++) {
            assertEquals(dtos.get(i - 1).getId(), entityList.get(i - 1).getId());
        }
    }
}
