package kz.kazdream.demo.onboard.api.v1;

import com.fasterxml.jackson.databind.ObjectMapper;
import kz.kazdream.demo.onboard.db.dto.UserDto;
import kz.kazdream.demo.onboard.db.entity.User;
import kz.kazdream.demo.onboard.db.mapper.UserMapper;
import kz.kazdream.demo.onboard.db.service.UserService;
import kz.kazdream.demo.onboard.rabbitmq.ProducerHelper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(value = {UserApi.class})
class UserApiTest {
    @Autowired
    private MockMvc mvc;

    @MockBean
    private UserService service;

    @MockBean
    private ProducerHelper helper;

    @MockBean
    private UserMapper mapper;

    private ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void get_user_status200() throws Exception {

        mvc.perform(get("/users/").contentType(APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void get_user_should_return_list() throws Exception {
        User user = new User();
        user.setLogin("User");
        user.setId(1);
        List<User> users = new ArrayList<>();
        users.add(user);

        UserDto userDto = new UserDto();
        userDto.setLogin("User");
        userDto.setId(1);

        List<UserDto> userDtos = new ArrayList<>();
        userDtos.add(userDto);

        when(mapper.toDtoList(users)).thenReturn(userDtos);

        given(service.findAll()).willReturn(users);

        ResultActions resultActions =
                mvc.perform(get("/users/").contentType(APPLICATION_JSON))
                        .andExpect(status().isOk());

        assertEquals(
                objectMapper.writeValueAsString(userDtos),
                resultActions.andReturn().getResponse().getContentAsString());
    }

    @Test
    void get_user_by_id_status_200() throws Exception {
        User user = new User();
        user.setLogin("User");
        user.setId(1);

        when(service.findById(user.getId())).thenReturn(user);

        mvc.perform(get("/users/" + user.getId()).contentType(APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void get_user_by_id() throws Exception {
        User user = new User();
        user.setLogin("User");
        user.setId(1);

        when(service.findById(user.getId())).thenReturn(user);

        String actual = mvc.perform(get("/users/" + user.getId()).contentType(APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();

        assertEquals(objectMapper.writeValueAsString(user), actual);
    }

    @Test
    void get_user_by_id_should_return_null() throws Exception {
        when(service.findById(Mockito.anyInt())).thenReturn(null);
        String contentAsString = mvc.perform(get("/users/0").contentType(APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();

        assertEquals("", contentAsString);
    }

    @Test
    void put_user_should_return_status200() throws Exception {
        UserDto dto = new UserDto();
        dto.setLogin("User");
        dto.setId(1);

        User editedUser = new User();
        editedUser.setId(1);
        editedUser.setLogin("Edited");

        when(mapper.toEntity(any())).thenReturn(editedUser);
        when(service.findById(dto.getId())).thenReturn(editedUser);
        when(service.save(editedUser)).thenReturn(editedUser);


        mvc.perform(put("/users/1").contentType(APPLICATION_JSON).content(objectMapper.writeValueAsBytes(dto)))
                .andExpect(status().isOk());
    }

    @Test
    void put_user_should_return_status201() throws Exception {
        UserDto dto = new UserDto();
        dto.setLogin("User");
        dto.setId(1);

        User editedUser = new User();
        editedUser.setId(1);
        editedUser.setLogin("Edited");

        when(mapper.toEntity(any())).thenReturn(editedUser);
        when(service.findById(dto.getId())).thenReturn(null);
        when(service.save(editedUser)).thenReturn(editedUser);


        mvc.perform(put("/users/1").contentType(APPLICATION_JSON).content(objectMapper.writeValueAsBytes(dto)))
                .andExpect(status().isCreated());
    }

    @Test
    void put_exist_user() throws Exception {
        UserDto dto = new UserDto();
        dto.setLogin("User");
        dto.setId(1);

        User editedUser = new User();
        editedUser.setId(1);
        editedUser.setLogin("Edited");

        when(mapper.toEntity(any())).thenReturn(editedUser);
        when(service.findById(dto.getId())).thenReturn(editedUser);
        when(service.save(editedUser)).thenReturn(editedUser);

        String actual = mvc.perform(put("/users/1").contentType(APPLICATION_JSON).content(objectMapper.writeValueAsBytes(dto)))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();

        assertEquals(objectMapper.writeValueAsString(editedUser), actual);
    }


    @Test
    void put_new_user() throws Exception {
        UserDto dto = new UserDto();
        dto.setLogin("User");
        dto.setId(1);

        User editedUser = new User();
        editedUser.setId(1);
        editedUser.setLogin("Edited");

        when(mapper.toEntity(any())).thenReturn(editedUser);
        when(service.findById(dto.getId())).thenReturn(null);
        when(service.save(editedUser)).thenReturn(editedUser);

        String actual = mvc.perform(
                put("/users/1")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(dto)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(objectMapper.writeValueAsString(editedUser), actual);
    }

    @Test
    void post_add_new_user_status201() throws Exception {
        UserDto dto = new UserDto();
        dto.setLogin("User");
        dto.setId(1);

        User entity = new User();
        entity.setLogin("User");
        entity.setId(1);

        when(mapper.toEntity(dto)).thenReturn(entity);
        when(service.save(entity)).thenReturn(entity);

        mvc.perform(
                post("/users/")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(dto)))
                .andExpect(status().isCreated());

    }


    @Test
    void post_add_new_user() throws Exception {
        UserDto dto = new UserDto();
        dto.setLogin("User");
        dto.setId(1);

        User entity = new User();
        entity.setLogin("User");
        entity.setId(1);

        when(mapper.toEntity(any())).thenReturn(entity);
        when(service.save(entity)).thenReturn(entity);

        String actual = mvc.perform(
                post("/users/")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(dto)))
                .andExpect(status().isCreated()).andReturn().getResponse().getContentAsString();

        assertEquals(objectMapper.writeValueAsString(entity), actual);
    }

    @Test
    void delete_user_by_id_status204() throws Exception {

        User user = new User();
        user.setId(1);

        when(service.findById(1)).thenReturn(user);

        mvc.perform(
                delete("/users/1").contentType(APPLICATION_JSON)
        ).andExpect(status().isNoContent());

        verify(service,times(1)).delete(user);
    }

    @Test
    void delete_user_by_id_status404() throws Exception {



        when(service.findById(1)).thenReturn(null);

        mvc.perform(
                delete("/users/1").contentType(APPLICATION_JSON)
        ).andExpect(status().isNotFound());

    }
}