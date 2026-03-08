package com.barber.system.security;

import com.barber.system.model.BarberShop;
import com.barber.system.model.User;
import com.barber.system.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class TenantContextService {

    private final UserRepository userRepository;

    @Autowired
    public TenantContextService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public BarberShop getCurrentBarberShop() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username;
        if (principal instanceof UserDetails) {
            username = ((UserDetails) principal).getUsername();
        } else {
            username = principal.toString();
        }
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé dans le contexte de sécurité."));
        return user.getBarberShop();
    }
}
