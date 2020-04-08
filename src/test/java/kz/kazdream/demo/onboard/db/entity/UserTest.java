package kz.kazdream.demo.onboard.db.entity;

import kz.kazdream.demo.onboard.db.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
class UserTest {

    @Autowired
    private UserRepository repository;

    @Test
    void add_first_user_success() {
        User expected = userCreator("User1");

        repository.save(expected);

        assertNotNull(expected.getId());
    }

    @Test
    void find_first_user_success() {

        User expected = userCreator("User1");

        repository.save(expected);

        User actual = repository.findById(expected.getId()).orElse(null);

        assertEquals(expected, actual);
    }

    @Test
    void find_user_should_return_zero() {

        List<User> users = iteratorToList(repository.findAll().iterator());

        assertEquals(0, users.size());
    }

    @Test
    void add_list_users() {
        List<User> expected = new ArrayList<>();
        expected.add(userCreator("user1"));
        expected.add(userCreator("user2"));
        expected.add(userCreator("user3"));
        expected.add(userCreator("user4"));
        expected.add(userCreator("user5"));
        expected.add(userCreator("user6"));
        expected.add(userCreator("user7"));

        repository.saveAll(expected);

        List<User> actual = iteratorToList(repository.findAll().iterator());

        assertEquals(expected, actual);
    }

    @Test
    void remove_user() {
        User expected = userCreator("Removed");
        User save = repository.save(expected);

        repository.delete(save);

        User user = repository.findById(save.getId()).orElse(null);
        Assertions.assertNull(user);
    }

    @Test
    void remove_list_users() {
        List<User> expected = new ArrayList<>();
        expected.add(userCreator("user1"));
        expected.add(userCreator("user2"));
        expected.add(userCreator("user3"));
        expected.add(userCreator("user4"));
        expected.add(userCreator("user5"));
        expected.add(userCreator("user6"));
        expected.add(userCreator("user7"));

        repository.saveAll(expected);

        repository.deleteAll();

        int actualSize = iteratorToList(repository.findAll().iterator()).size();
        assertEquals(0, actualSize);
    }

    @Test
    void edit_user() {

        String newUserName = "NewUserName";
        String oldUserName = "OldUserName";
        User expected = userCreator(oldUserName);
        repository.save(expected);

        expected.setLastName(newUserName);
        repository.save(expected);

        User actual = repository.findById(expected.getId()).orElse(null);

        assertNotNull(actual);
        assertEquals(newUserName, actual.getLastName());
    }

    private List<User> iteratorToList(Iterator<User> iterator) {
        List<User> result = new ArrayList<>();
        iterator.forEachRemaining(result::add);
        return result;
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
