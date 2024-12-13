package ru.kata.spring.boot_security.demo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.dao.RoleDao;
import ru.kata.spring.boot_security.demo.models.Role;

import java.util.List;

@Service
@Transactional
public class RoleServiceImp implements RoleService {
    private final RoleDao roleDao;

    @Autowired
    public RoleServiceImp(RoleDao roleDao) {
        this.roleDao = roleDao;
    }

    @Override
    public Role saveIfNotExists(Role role) {
        Role existingRole = roleDao.findByName(role.getName());
        if (existingRole == null) {
            existingRole = roleDao.save(role);
        }
        return existingRole;
    }

    @Override
    public List<Role> listRoles() {
        return roleDao.findAll();
    }
}
