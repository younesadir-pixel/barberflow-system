package com.barber.system.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "clients")
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "barber_shop_id")
    private BarberShop barberShop;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String phone;

    private String email;

    @Column(nullable = false)
    private LocalDateTime registrationDate = LocalDateTime.now();

    @OneToMany(mappedBy = "client", fetch = FetchType.LAZY)
    private List<Appointment> appointments = new ArrayList<>();

    public Client() {}

    public Client(Long id, String name, String phone, String email, LocalDateTime registrationDate, List<Appointment> appointments) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.registrationDate = registrationDate;
        this.appointments = appointments;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public BarberShop getBarberShop() { return barberShop; }
    public void setBarberShop(BarberShop barberShop) { this.barberShop = barberShop; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public LocalDateTime getRegistrationDate() { return registrationDate; }
    public void setRegistrationDate(LocalDateTime registrationDate) { this.registrationDate = registrationDate; }

    public List<Appointment> getAppointments() { return appointments; }
    public void setAppointments(List<Appointment> appointments) { this.appointments = appointments; }
}
