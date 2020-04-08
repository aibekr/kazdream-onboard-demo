package kz.kazdream.demo.onboard.db.mapper;

import kz.kazdream.demo.onboard.db.dto.UserDto;
import kz.kazdream.demo.onboard.db.entity.User;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class UserMapper extends AbstractMapper<User, UserDto> {
    public UserMapper(ModelMapper mapper) {
        super(User.class, UserDto.class);
        this.mapper = mapper;
    }

    @Override
    public List<UserDto> toDtoList(List<User> entityList) {
        List<UserDto> dtos = new ArrayList<>();
        for (User entity : entityList) {
            dtos.add(toDto(entity));
        }
        return dtos;
    }

    @Override
    public List<User> toEntityList(List<UserDto> entityDto) {
        List<User> listEntity = new ArrayList<>();
        for (UserDto dto : entityDto) {
            listEntity.add(toEntity(dto));
        }
        return listEntity;
    }
}
