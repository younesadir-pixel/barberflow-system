package com.barber.system.config;

import com.barber.system.model.*;
import com.barber.system.model.enums.AppointmentStatus;
import com.barber.system.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initData(UserRepository userRepository, 
                               ClientRepository clientRepository, 
                               AppointmentRepository appointmentRepository,
                               ServiceRepository serviceRepository,
                               BarberShopRepository barberShopRepository,
                               PasswordEncoder passwordEncoder) {
        return args -> {
            // 0. Create Default Barber Shop
            BarberShop defaultShop = barberShopRepository.findByName("Style Squire HQ").orElseGet(() -> {
                BarberShop shop = new BarberShop();
                shop.setName("Style Squire HQ");
                shop.setAddress("Casablanca, Morocco");
                shop.setPhone("0522123456");
                shop.setLicenseKey("HQ-DEFAULT-001");
                return barberShopRepository.save(shop);
            });

            // 1. Force Update Admin Password
            User admin = userRepository.findByUsername("admin").orElseGet(() -> {
                User newAdmin = new User();
                newAdmin.setUsername("admin");
                newAdmin.setFullName("Ahmed El Mansouri");
                newAdmin.setRole("ADMIN");
                newAdmin.setBarberShop(defaultShop);
                return newAdmin;
            });
            admin.setPassword(passwordEncoder.encode("123123123aze"));
            admin.setBarberShop(defaultShop);
            userRepository.save(admin);

            // 2. Seed Services
            com.barber.system.model.Service sHaircut, sBeard, sVIP, sKids;
            if (serviceRepository.count() == 0) {
                sHaircut = new com.barber.system.model.Service("Classic Haircut", new BigDecimal("50.00"), 30, "Hair", "#D4AF37");
                sHaircut.setBarberShop(defaultShop);
                sHaircut = serviceRepository.save(sHaircut);

                sBeard = new com.barber.system.model.Service("Beard Trim", new BigDecimal("30.00"), 20, "Beard", "#ffffff");
                sBeard.setBarberShop(defaultShop);
                sBeard = serviceRepository.save(sBeard);

                sVIP = new com.barber.system.model.Service("VIP Treatment", new BigDecimal("120.00"), 60, "Premium", "#FFA500");
                sVIP.setBarberShop(defaultShop);
                sVIP = serviceRepository.save(sVIP);

                sKids = new com.barber.system.model.Service("Kid's Haircut", new BigDecimal("40.00"), 25, "Hair", "#ADD8E6");
                sKids.setBarberShop(defaultShop);
                sKids = serviceRepository.save(sKids);
            } else {
                List<com.barber.system.model.Service> allServices = serviceRepository.findAll();
                sHaircut = allServices.get(0);
                sBeard = allServices.size() > 1 ? allServices.get(1) : sHaircut;
                sVIP = allServices.size() > 2 ? allServices.get(2) : sHaircut;
                sKids = allServices.size() > 3 ? allServices.get(3) : sHaircut;
            }

            // 3. Clear existing appointments
            appointmentRepository.deleteAll();

            if (clientRepository.count() == 0) {
                Client c1 = new Client();
                c1.setName("Youssef Alami");
                c1.setPhone("0612345678");
                c1.setRegistrationDate(LocalDateTime.now().minusDays(10));
                c1.setBarberShop(defaultShop);

                Client c2 = new Client();
                c2.setName("Omar Bennani");
                c2.setPhone("0698765432");
                c2.setRegistrationDate(LocalDateTime.now().minusDays(5));
                c2.setBarberShop(defaultShop);

                Client c3 = new Client();
                c3.setName("Mehdi Tazi");
                c3.setPhone("0655443322");
                c3.setRegistrationDate(LocalDateTime.now().minusDays(2));
                c3.setBarberShop(defaultShop);

                clientRepository.saveAll(Arrays.asList(c1, c2, c3));

                // Create Sample Appointments for Today
                LocalDateTime now = LocalDateTime.now();

                Appointment a1 = new Appointment();
                a1.setBarberShop(defaultShop);
                a1.setClient(c1);
                a1.setUser(admin);
                a1.setAppointmentDateTime(now.withHour(9).withMinute(0));
                a1.setService(sHaircut);
                a1.setPrice(sHaircut.getPrice());
                a1.setStatus(AppointmentStatus.COMPLETED);

                Appointment a2 = new Appointment();
                a2.setBarberShop(defaultShop);
                a1.setBarberShop(defaultShop);
                a2.setClient(c2);
                a2.setUser(admin);
                a2.setAppointmentDateTime(now.withHour(10).withMinute(30));
                a2.setService(sBeard);
                a2.setPrice(sBeard.getPrice());
                a2.setStatus(AppointmentStatus.COMPLETED);

                Appointment a3 = new Appointment();
                a3.setBarberShop(defaultShop);
                a3.setClient(c3);
                a3.setUser(admin);
                a3.setAppointmentDateTime(now.withHour(14).withMinute(0));
                a3.setService(sVIP);
                a3.setPrice(sVIP.getPrice());
                a3.setStatus(AppointmentStatus.SCHEDULED);

                Appointment a4 = new Appointment();
                a4.setBarberShop(defaultShop);
                a4.setClient(c1);
                a4.setUser(admin);
                a4.setAppointmentDateTime(now.withHour(16).withMinute(15));
                a4.setService(sHaircut);
                a4.setPrice(sHaircut.getPrice());
                a4.setStatus(AppointmentStatus.SCHEDULED);

                Appointment a5 = new Appointment();
                a5.setBarberShop(defaultShop);
                a5.setClient(c2);
                a5.setUser(admin);
                a5.setAppointmentDateTime(now.withHour(18).withMinute(45));
                a5.setService(sKids);
                a5.setPrice(sKids.getPrice());
                a5.setStatus(AppointmentStatus.SCHEDULED);

                appointmentRepository.saveAll(Arrays.asList(a1, a2, a3, a4, a5));
                
                System.out.println(">>> Multi-Tenant Database Seeded Successfully!");
            }
        };
    }
}
