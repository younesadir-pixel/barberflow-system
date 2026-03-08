package com.barber.system.model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "barber_shops")
public class BarberShop {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    private String address;
    private String phone;

    @Column(unique = true)
    private String licenseKey;

    @OneToMany(mappedBy = "barberShop", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<User> users = new ArrayList<>();

    @OneToMany(mappedBy = "barberShop", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Client> clients = new ArrayList<>();

    @OneToMany(mappedBy = "barberShop", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Service> services = new ArrayList<>();

    @OneToMany(mappedBy = "barberShop", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Appointment> appointments = new ArrayList<>();

    public BarberShop() {}

    public BarberShop(String name, String address, String phone, String licenseKey) {
        this.name = name;
        this.address = address;
        this.phone = phone;
        this.licenseKey = licenseKey;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getLicenseKey() { return licenseKey; }
    public void setLicenseKey(String licenseKey) { this.licenseKey = licenseKey; }

    public List<User> getUsers() { return users; }
    public void setUsers(List<User> users) { this.users = users; }

    public List<Client> getClients() { return clients; }
    public void setClients(List<Client> clients) { this.clients = clients; }

    public List<Service> getServices() { return services; }
    public void setServices(List<Service> services) { this.services = services; }

    public List<Appointment> getAppointments() { return appointments; }
    public void setAppointments(List<Appointment> appointments) { this.appointments = appointments; }
}
