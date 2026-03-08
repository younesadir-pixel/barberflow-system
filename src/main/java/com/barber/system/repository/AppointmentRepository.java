package com.barber.system.repository;

import com.barber.system.model.Appointment;
import com.barber.system.model.BarberShop;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional; // Added import for Optional

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    
    List<Appointment> findByBarberShop(BarberShop barberShop);

    // For today's schedule or any range (e.g., this week)
    List<Appointment> findByBarberShopAndAppointmentDateTimeBetween(BarberShop barberShop, LocalDateTime start, LocalDateTime end);
    
    // Quick lookup by client phone
    List<Appointment> findByBarberShopAndClientPhone(BarberShop barberShop, String phone);
}

// The following interfaces are added based on the instruction to update ClientRepository and ServiceRepository.
// In a real project, these would typically be in their own separate files (e.g., ClientRepository.java, ServiceRepository.java).
// For the purpose of this exercise, they are included here as per the provided "Code Edit" structure,
// assuming the user wants to see the full context of the requested changes in one output.

// Assuming Client model exists and needs filtering by BarberShop
// @Repository
// public interface ClientRepository extends JpaRepository<Client, Long> {
//     List<Client> findByBarberShop(BarberShop barberShop);
//     Optional<Client> findByBarberShopAndPhone(BarberShop barberShop, String phone);
//     boolean existsByBarberShopAndPhone(BarberShop barberShop, String phone);
//     boolean existsByBarberShopAndEmail(BarberShop barberShop, String email);
// }

// Assuming Service model exists and needs filtering by BarberShop
// @Repository
// public interface ServiceRepository extends JpaRepository<Service, Long> {
//     List<Service> findByBarberShop(BarberShop barberShop);
//     Optional<Service> findByBarberShopAndName(BarberShop barberShop, String name);
//     boolean existsByBarberShopAndName(BarberShop barberShop, String name);
// }
