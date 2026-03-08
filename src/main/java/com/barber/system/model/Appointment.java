package com.barber.system.model;

import com.barber.system.model.enums.AppointmentStatus;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "appointments")
public class Appointment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "barber_shop_id", nullable = false)
    private BarberShop barberShop;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private LocalDateTime appointmentDateTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "service_id", nullable = false)
    private Service service;

    @Column(nullable = false)
    private BigDecimal price;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AppointmentStatus status = AppointmentStatus.SCHEDULED;

    private String notes;

    public Appointment() {}

    public Appointment(Long id, Client client, User user, LocalDateTime appointmentDateTime, Service service, BigDecimal price, AppointmentStatus status, String notes) {
        this.id = id;
        this.client = client;
        this.user = user;
        this.appointmentDateTime = appointmentDateTime;
        this.service = service;
        this.price = price;
        this.status = status;
        this.notes = notes;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public BarberShop getBarberShop() { return barberShop; }
    public void setBarberShop(BarberShop barberShop) { this.barberShop = barberShop; }

    public Client getClient() { return client; }
    public void setClient(Client client) { this.client = client; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public LocalDateTime getAppointmentDateTime() { return appointmentDateTime; }
    public void setAppointmentDateTime(LocalDateTime appointmentDateTime) { this.appointmentDateTime = appointmentDateTime; }

    public Service getService() { return service; }
    public void setService(Service service) { this.service = service; }

    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }

    public AppointmentStatus getStatus() { return status; }
    public void setStatus(AppointmentStatus status) { this.status = status; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
}
