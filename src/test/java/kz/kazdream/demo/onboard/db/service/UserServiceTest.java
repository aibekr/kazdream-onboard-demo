package kz.kazdream.demo.onboard.db.service;

import kz.kazdream.demo.onboard.db.entity.User;
import kz.kazdream.demo.onboard.db.repository.UserRepository;
import kz.kazdream.demo.onboard.db.service.impl.UserServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@DataJpaTest
class UserServiceTest {
    @TestConfiguration
    static class UserServiceImplTestContextConfiguration {

        @Bean
        public UserService userService(UserRepository repository) {
            return new UserServiceImpl(repository);
        }
    }

    @Autowired
    private UserService userService;

    @MockBean
    private UserRepository repository;

    @Test
    void save_user() {
        User expected = userCreator("User1");
        expected.setId(1);

        when(repository.save(expected)).thenReturn(expected);

        User actual = userService.save(expected);

        assertEquals(expected, actual);
    }

    @Test
    void save_all_users() {
        List<User> users = new ArrayList();
        users.add(userCreator("User1"));
        users.add(userCreator("User2"));
        users.add(userCreator("User3"));
        users.add(userCreator("User4"));
        users.add(userCreator("User5"));
        users.add(userCreator("User6"));
        users.add(userCreator("User7"));

        when(repository.saveAll(users)).thenReturn(users);

        List<User> actual = userService.saveAll(users);

        assertEquals(users.size(), actual.size());
        assertEquals(users, actual);
    }

    @Test
    void find_all_users() {
        List<User> users = new ArrayList();
        users.add(userCreator("User1"));
        users.add(userCreator("User2"));
        users.add(userCreator("User3"));
        users.add(userCreator("User4"));
        users.add(userCreator("User5"));
        users.add(userCreator("User6"));
        users.add(userCreator("User7"));

        when(repository.findAll()).thenReturn(users);

        List<User> actual = userService.findAll();

        assertEquals(users.size(), actual.size());
        assertEquals(users, actual);
    }

    @Test
    void find_by_id_user() {
        User user = userCreator("User");
        user.setId(1);
        Optional<User> expected = Optional.of(user);
        when(repository.findById(1)).thenReturn(expected);

        User actual = userService.findById(1);

        assertEquals(user, actual);
    }

    @Test
    void delete_user() {
        User user = userCreator("Removed");
        user.setId(1);

        userService.delete(user);
        verify(repository, times(1)).delete(user);
    }

    @Test
    void delete_all_users() {
        when(repository.findAll()).thenReturn(new ArrayList<>());

        userService.deleteAll();

        int size = userService.findAll().size();
        assertEquals(0, size);
    }

    @Test
    void edit_user() {
        User user = userCreator("User");
        user.setId(1);
        User editedUser = userCreator("Edited");
        when(repository.save(user)).thenReturn(editedUser);

        User actual = userService.edit(user);

        assertEquals(editedUser, actual);
    }

    private User userCreator(String name) {
        User user = new User();
        user.setFirstName(name);
        user.setLastName(name);
        user.setEmail(name);
        user.setLogin(name);

        return user;
    }
}
