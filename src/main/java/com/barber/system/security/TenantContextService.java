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
        if (SecurityContextHolder.getContext().getAuthentication() == null) {
            return null;
        }
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal == null) {
            return null;
        }
        
        String username;
        if (principal instanceof UserDetails) {
            username = ((UserDetails) principal).getUsername();
        } else {
            username = principal.toString();
        }
        
        if ("anonymousUser".equals(username)) {
            return null;
        }

        return userRepository.findByUsername(username)
                .map(User::getBarberShop)
                .orElse(null);
    }
}
