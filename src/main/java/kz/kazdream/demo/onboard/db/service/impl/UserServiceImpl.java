package kz.kazdream.demo.onboard.db.service.impl;

import kz.kazdream.demo.onboard.db.entity.User;
import kz.kazdream.demo.onboard.db.repository.UserRepository;
import kz.kazdream.demo.onboard.db.service.UserService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository repository;

    public UserServiceImpl(UserRepository repository) {
        this.repository = repository;
    }


    @Override
    public User save(User user) {
        return repository.save(user);
    }

    @Override
    public List<User> saveAll(List<User> users) {
        return repository.saveAll(users);
    }

    @Override
    public List<User> findAll() {
        return repository.findAll();
    }

    @Override
    public User findById(int id) {
        return repository.findById(id).orElse(null);
    }

    @Override
    public void delete(User user) {
        repository.delete(user);
    }

    @Override
    public void deleteAll() {
        repository.deleteAll();
    }

    @Override
    public User edit(User user) {
        return repository.save(user);
    }
}
