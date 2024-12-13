package ru.kata.spring.boot_security.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.services.RoleService;
import ru.kata.spring.boot_security.demo.services.UserService;

import javax.validation.Valid;
import java.security.Principal;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin")
public class AdminsController {
    private final UserService userService;
    private final RoleService roleService;

    @Autowired
    public AdminsController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @GetMapping()
    public String mainPage(Model model, Principal principal) {
        modelAttributes(model, new User(), principal);
        return "adminPanel";
    }

    @PostMapping("/delete")
    public String deleteUser(@ModelAttribute("userModel") User userModel, Model model, Principal principal) {
        userService.deleteUser(userModel.getId());
        modelAttributes(model, new User(), principal);
        return "adminPanel";
    }

    @PostMapping("/edit")
    public String updateUser(@Valid @ModelAttribute("userModel") User userModel, BindingResult bindingResult, Model model, Principal principal) {
        userService.saveUser(userModel, bindingResult);
        modelAttributes(model, userModel, principal);
        return "adminPanel";
    }

    private void modelAttributes(Model model, User user, Principal principal) {
        model.addAttribute("username", principal.getName());
        model.addAttribute("roles", ((Authentication)principal).getAuthorities().
                stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()));
        model.addAttribute("availableRoles", roleService.listRoles());
        model.addAttribute("users", userService.listUsers());
        model.addAttribute("userModel", user);
    }
}