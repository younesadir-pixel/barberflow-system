package com.barber.system.repository;

import com.barber.system.model.BarberShop;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BarberShopRepository extends JpaRepository<BarberShop, Long> {
    Optional<BarberShop> findByName(String name);
    Optional<BarberShop> findByLicenseKey(String licenseKey);
}
