package ru.kata.spring.boot_security.demo.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.kata.spring.boot_security.demo.models.Role;

public interface RoleDao extends JpaRepository<Role, Integer> {
    Role findByName(String name);
}