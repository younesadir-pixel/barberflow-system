package com.barber.system.service;

import com.barber.system.model.Service;
import com.barber.system.model.BarberShop;
import com.barber.system.repository.ServiceRepository;
import com.barber.system.security.TenantContextService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@org.springframework.stereotype.Service
public class ServiceService {

    private final ServiceRepository serviceRepository;
    private final TenantContextService tenantContextService;

    @Autowired
    public ServiceService(ServiceRepository serviceRepository, TenantContextService tenantContextService) {
        this.serviceRepository = serviceRepository;
        this.tenantContextService = tenantContextService;
    }

    @Transactional(readOnly = true)
    public List<Service> getAllServices() {
        return serviceRepository.findByBarberShop(tenantContextService.getCurrentBarberShop());
    }
}
