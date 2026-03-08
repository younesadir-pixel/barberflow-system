package com.barber.system.service;

import com.barber.system.model.Appointment;
import com.barber.system.model.BarberShop;
import com.barber.system.model.Client;
import com.barber.system.model.User;
import com.barber.system.model.enums.AppointmentStatus;
import com.barber.system.repository.AppointmentRepository;
import com.barber.system.repository.ClientRepository;
import com.barber.system.repository.ServiceRepository;
import com.barber.system.repository.UserRepository;
import com.barber.system.security.TenantContextService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final ClientRepository clientRepository;
    private final ServiceRepository serviceRepository;
    private final TenantContextService tenantContextService;

    @Autowired
    public AppointmentService(AppointmentRepository appointmentRepository, 
                              ClientRepository clientRepository, 
                              ServiceRepository serviceRepository,
                              TenantContextService tenantContextService) {
        this.appointmentRepository = appointmentRepository;
        this.clientRepository = clientRepository;
        this.serviceRepository = serviceRepository;
        this.tenantContextService = tenantContextService;
    }

    @Transactional
    public Appointment bookAppointment(Appointment appointment, String clientPhone, String clientName, String clientEmail, Long serviceId) {
        BarberShop shop = tenantContextService.getCurrentBarberShop();
        
        // 1. Fetch Service
        com.barber.system.model.Service service = serviceRepository.findById(serviceId)
                .filter(s -> s.getBarberShop().getId().equals(shop.getId()))
                .orElseThrow(() -> new RuntimeException("Service introuvable ou n'appartient pas à votre salon."));

        // 2. Enforce strict uniqueness (User requirement: throw error if exists in THIS shop)
        if (clientRepository.existsByBarberShopAndPhone(shop, clientPhone)) {
            throw new RuntimeException("Un client avec ce numéro de téléphone (" + clientPhone + ") existe déjà dans votre salon.");
        }
        
        if (clientEmail != null && !clientEmail.trim().isEmpty()) {
            if (clientRepository.existsByBarberShopAndEmail(shop, clientEmail)) {
                throw new RuntimeException("Un client avec cette adresse e-mail (" + clientEmail + ") existe déjà dans votre salon.");
            }
        }

        // 3. Create new client
        Client newClient = new Client();
        newClient.setName(clientName);
        newClient.setPhone(clientPhone);
        newClient.setEmail(clientEmail);
        newClient.setRegistrationDate(LocalDateTime.now());
        newClient.setBarberShop(shop);
        Client savedClient = clientRepository.save(newClient);

        appointment.setBarberShop(shop);
        appointment.setClient(savedClient);
        appointment.setService(service);
        appointment.setPrice(service.getPrice()); // Default price from service
        return appointmentRepository.save(appointment);
    }

    @Transactional(readOnly = true)
    public BigDecimal calculateTotalEarningsForToday() {
        BarberShop shop = tenantContextService.getCurrentBarberShop();
        LocalDateTime startOfDay = LocalDateTime.of(LocalDate.now(), LocalTime.MIN);
        LocalDateTime endOfDay = LocalDateTime.of(LocalDate.now(), LocalTime.MAX);

        List<Appointment> todaysAppointments = appointmentRepository.findByBarberShopAndAppointmentDateTimeBetween(shop, startOfDay, endOfDay);

        return todaysAppointments.stream()
                .filter(a -> a.getStatus() == AppointmentStatus.COMPLETED)
                .map(Appointment::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Transactional(readOnly = true)
    public List<Appointment> getAppointmentsForToday() {
        BarberShop shop = tenantContextService.getCurrentBarberShop();
        LocalDateTime startOfDay = LocalDateTime.of(LocalDate.now(), LocalTime.MIN);
        LocalDateTime endOfDay = LocalDateTime.of(LocalDate.now(), LocalTime.MAX);
        return appointmentRepository.findByBarberShopAndAppointmentDateTimeBetween(shop, startOfDay, endOfDay);
    }

    @Transactional
    public void updateStatus(Long appointmentId, AppointmentStatus status) {
        appointmentRepository.findById(appointmentId).ifPresent(appointment -> {
            appointment.setStatus(status);
            appointmentRepository.save(appointment);
        });
    }

    @Transactional(readOnly = true)
    public List<Appointment> getAllAppointments() {
        BarberShop shop = tenantContextService.getCurrentBarberShop();
        return appointmentRepository.findByBarberShop(shop);
    }
}
