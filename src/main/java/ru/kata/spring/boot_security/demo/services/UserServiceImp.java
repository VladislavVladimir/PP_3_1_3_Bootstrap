package ru.kata.spring.boot_security.demo.services;

import org.hibernate.Hibernate;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import ru.kata.spring.boot_security.demo.dao.UserDao;
import ru.kata.spring.boot_security.demo.models.User;

import java.util.List;

@Service
@Transactional
public class UserServiceImp implements UserService, UserDetailsService {
    private final UserDao userDao;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImp(UserDao userDao, PasswordEncoder passwordEncoder) {
        this.userDao = userDao;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public List<User> listUsers() {
        return userDao.findAll();
    }

    @Override
    public User getUser(Long id) {
        return userDao.getById(id);
    }

    @Override
    public void deleteUser(Long id) {
        userDao.deleteById(id);
    }

    @Override
    public User getUserByEmail(String email) {
        return userDao.findByEmail(email);
    }

    @Override
    public void saveUser(User user) {
        if ((user.getId() == 0) || (user.getId() > 0 && !user.getPassword().trim().isEmpty())) { //если нужно изменить пароль
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        } else { //если нужно оставить старый пароль
            User updateUser = userDao.getById(user.getId());
            user.setPassword(updateUser.getPassword());
        }

        userDao.save(user);
    }

    @Override
    public void saveUser(User user, BindingResult bindingResult) {
        if (hasProblemValid(user, bindingResult)) return;

        saveUser(user);
    }

    private boolean hasProblemValid(User user, BindingResult bindingResult) {
        if ((!user.getPassword().trim().isEmpty() && user.getPassword().trim().length()<4) ||   //если пароль 1-3 символа
                (user.getPassword().trim().isEmpty() && user.getId()==0)) {  //или 0 и user только создается
            bindingResult.rejectValue("password", "error.user", "Пароль должен содержать минимум 4 символа");
        }
        checkUniqueField(user, bindingResult, userDao.findByEmail(user.getEmail()), "email", "Email уже занят");

        return bindingResult.hasErrors();
    }

    private void checkUniqueField(User user, BindingResult bindingResult, User existing, String fieldName, String errorMsg) {
        if (existing != null && existing.getId() != user.getId()) {
            bindingResult.rejectValue(fieldName, "error.user", errorMsg);
        }
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userDao.findByEmail(email);
        if (user == null) {
            throw new UsernameNotFoundException(String.format("User %s not found", email));
        }
        Hibernate.initialize(user.getRoles()); //загружаем роли
        return user;
    }
}
