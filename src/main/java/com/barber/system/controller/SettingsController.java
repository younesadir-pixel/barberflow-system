package com.barber.system.controller;

import com.barber.system.model.User;
import com.barber.system.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class SettingsController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public SettingsController(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/settings")
    public String settings(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        if (userDetails != null) {
            userRepository.findByUsername(userDetails.getUsername()).ifPresent(user -> {
                model.addAttribute("user", user);
            });
        }
        return "settings";
    }

    @PostMapping("/settings/update-profile")
    public String updateProfile(@AuthenticationPrincipal UserDetails userDetails,
                                @RequestParam String fullName,
                                RedirectAttributes redirectAttributes) {
        if (userDetails != null) {
            userService.getUserByUsername(userDetails.getUsername()).ifPresent(user -> {
                user.setFullName(fullName);
                userService.saveUser(user);
                redirectAttributes.addFlashAttribute("successMessage", "Profil mis à jour avec succès !");
            });
        }
        return "redirect:/settings";
    }

    @PostMapping("/settings/update-password")
    public String updatePassword(@AuthenticationPrincipal UserDetails userDetails,
                                 @RequestParam String currentPassword,
                                 @RequestParam String newPassword,
                                 RedirectAttributes redirectAttributes) {
        if (userDetails != null) {
            User user = userRepository.findByUsername(userDetails.getUsername()).orElse(null);
            if (user != null) {
                if (passwordEncoder.matches(currentPassword, user.getPassword())) {
                    user.setPassword(passwordEncoder.encode(newPassword));
                    userRepository.save(user);
                    redirectAttributes.addFlashAttribute("successMessage", "Mot de passe changé avec succès !");
                } else {
                    redirectAttributes.addFlashAttribute("errorMessage", "Le mot de passe actuel est incorrect.");
                }
            }
        }
        return "redirect:/settings";
    }
}
