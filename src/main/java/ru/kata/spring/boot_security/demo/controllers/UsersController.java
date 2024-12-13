package ru.kata.spring.boot_security.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.services.RoleService;
import ru.kata.spring.boot_security.demo.services.UserService;

import java.security.Principal;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/user")
public class UsersController {
    private final UserService userService;
    private final RoleService roleService;

    @Autowired
    public UsersController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @GetMapping()
    public String userProfile(Model userModel, Principal principal) {
        userModel.addAttribute("username", principal.getName());
        userModel.addAttribute("roles", ((Authentication)principal).getAuthorities().
                stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()));
        userModel.addAttribute("availableRoles", roleService.listRoles());
        userModel.addAttribute("userModel", userService.getUserByEmail(principal.getName()));
        return "userPanel";
    }
}