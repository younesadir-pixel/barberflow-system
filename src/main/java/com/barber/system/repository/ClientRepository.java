package com.barber.system.repository;

import com.barber.system.model.Client;
import com.barber.system.model.BarberShop;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {
    List<Client> findByBarberShop(BarberShop barberShop);
    Optional<Client> findByBarberShopAndPhone(BarberShop barberShop, String phone);
    boolean existsByBarberShopAndPhone(BarberShop barberShop, String phone);
    boolean existsByBarberShopAndEmail(BarberShop barberShop, String email);
}
