package kz.kazdream.demo.onboard.db.service;

import kz.kazdream.demo.onboard.db.entity.User;

import java.util.List;

public interface UserService {
    User save(User user);

    List<User> saveAll(List<User> users);

    List<User> findAll();

    User findById(int id);

    void delete(User user);

    void deleteAll();

    User edit(User user);
}
