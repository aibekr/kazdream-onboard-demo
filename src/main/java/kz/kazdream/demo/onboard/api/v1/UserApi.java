package kz.kazdream.demo.onboard.api.v1;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import kz.kazdream.demo.onboard.db.dto.UserDto;
import kz.kazdream.demo.onboard.db.dto.UserDto;
import kz.kazdream.demo.onboard.db.entity.User;
import kz.kazdream.demo.onboard.db.entity.User;
import kz.kazdream.demo.onboard.db.mapper.UserMapper;
import kz.kazdream.demo.onboard.db.service.UserService;
import kz.kazdream.demo.onboard.rabbitmq.ProducerHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping("/users")
public class UserApi {

    private final UserService userService;

    private final UserMapper mapper;

    private final ProducerHelper helper;

    public UserApi(UserService userService, UserMapper mapper, ProducerHelper helper) {
        this.userService = userService;
        this.mapper = mapper;
        this.helper = helper;
    }

    @GetMapping
    public ResponseEntity<List<UserDto>> getUsers() {
        List<User> users = userService.findAll();

        return ResponseEntity.ok(mapper.toDtoList(users));
    }

    @GetMapping("{userId}")
    public ResponseEntity<User> getUser(@PathVariable int userId) {
        return ResponseEntity.ok(userService.findById(userId));
    }

    @PutMapping(value = "{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<User> editUser(@PathVariable int userId, @RequestBody UserDto userDto) {
        User user = mapper.toEntity(userDto);

        if (userService.findById(userId) == null) {
            return ResponseEntity.status(CREATED).body(userService.save(user));
        }

        return ResponseEntity.ok(userService.save(user));
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<User> createUser(@RequestBody UserDto userDto) {
        User user = mapper.toEntity(userDto);
        return ResponseEntity.status(CREATED).body(userService.save(user));
    }

    @PostMapping(value = "/rabbit",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<User> createUserRabbit(@RequestBody UserDto userDto) throws JsonProcessingException {
        User user = mapper.toEntity(userDto);
        ObjectMapper objectMapper = new ObjectMapper();
        helper.send("create_user",objectMapper.writeValueAsBytes(user));
        return ResponseEntity.status(CREATED).body(null);
    }
    
    @DeleteMapping("{userId}")
    public ResponseEntity<User> deleteUser(@PathVariable int userId){
        User user = userService.findById(userId);
        if (user == null) {
            return ResponseEntity.status(NOT_FOUND).body(null);
        }
        userService.delete(user);

        return ResponseEntity.status(NO_CONTENT).body(null);
    }
}
