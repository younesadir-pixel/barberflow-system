package com.barber.system.controller;

import com.barber.system.model.BarberShop;
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
public class RegistrationController {

    private final UserService userService;

    @Autowired
    public RegistrationController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("salon", new BarberShop());
        model.addAttribute("admin", new User());
        return "register";
    }

    @PostMapping("/register")
    public String registerSalon(@ModelAttribute BarberShop salon, 
                                @ModelAttribute User admin, 
                                RedirectAttributes redirectAttributes) {
        try {
            userService.registerNewSalon(salon, admin);
            redirectAttributes.addFlashAttribute("successMessage", "Votre salon " + salon.getName() + " a été créé avec succès ! Vous pouvez maintenant vous connecter.");
            return "redirect:/login";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Une erreur est survenue lors de l'inscription : " + e.getMessage());
            return "redirect:/register";
        }
    }
}
