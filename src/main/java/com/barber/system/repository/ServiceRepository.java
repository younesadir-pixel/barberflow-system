package com.barber.system.repository;

import com.barber.system.model.Service;
import com.barber.system.model.BarberShop;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ServiceRepository extends JpaRepository<Service, Long> {
    List<Service> findByBarberShop(BarberShop barberShop);
}
