package ru.kata.spring.boot_security.demo.services;

import org.springframework.validation.BindingResult;
import ru.kata.spring.boot_security.demo.models.User;

import java.util.List;

public interface UserService {
    List<User> listUsers();
    User getUser(Long id);
    User getUserByEmail(String email);
    void deleteUser(Long id);
    void saveUser(User user);
    void saveUser(User user, BindingResult bindingResult);
}
