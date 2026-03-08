package com.barber.system.service;

import com.barber.system.model.User;
import com.barber.system.model.BarberShop;
import com.barber.system.repository.BarberShopRepository;
import com.barber.system.repository.UserRepository;
import com.barber.system.security.TenantContextService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final BarberShopRepository barberShopRepository;
    private final TenantContextService tenantContextService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, 
                       BarberShopRepository barberShopRepository,
                       TenantContextService tenantContextService,
                       PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.barberShopRepository = barberShopRepository;
        this.tenantContextService = tenantContextService;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional(readOnly = true)
    public List<User> getAllUsers() {
        return userRepository.findByBarberShop(tenantContextService.getCurrentBarberShop());
    }

    @Transactional(readOnly = true)
    public Optional<User> getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Transactional
    public User saveUser(User user) {
        // Hash password if it's not already hashed (usually for new users)
        if (user.getId() == null) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        
        if (user.getBarberShop() == null) {
            BarberShop currentShop = tenantContextService.getCurrentBarberShop();
            if (currentShop != null) {
                user.setBarberShop(currentShop);
            }
        }
        return userRepository.save(user);
    }

    @Transactional
    public void registerNewSalon(BarberShop shop, User admin) {
        // 1. Save Salon
        BarberShop savedShop = barberShopRepository.save(shop);
        
        // 2. Setup Admin
        admin.setBarberShop(savedShop);
        admin.setRole("ADMIN");
        admin.setPassword(passwordEncoder.encode(admin.getPassword()));
        
        userRepository.save(admin);
    }
}
