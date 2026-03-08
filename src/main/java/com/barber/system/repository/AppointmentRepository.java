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
