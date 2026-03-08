package com.barber.system.controller;

import com.barber.system.model.User;
import com.barber.system.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/barbers")
    public String listBarbers(Model model) {
        model.addAttribute("barbers", userService.getAllUsers());
        model.addAttribute("newUser", new User());
        return "barbers";
    }

    @PostMapping("/barbers/add")
    public String addBarber(@ModelAttribute User user, RedirectAttributes redirectAttributes) {
        try {
            if (user.getRole() == null || user.getRole().isEmpty()) {
                user.setRole("BARBER");
            }
            userService.saveUser(user);
            redirectAttributes.addFlashAttribute("successMessage", "Coiffeur " + user.getFullName() + " ajouté avec succès !");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Erreur lors de l'ajout : " + e.getMessage());
        }
        return "redirect:/barbers";
    }
}
