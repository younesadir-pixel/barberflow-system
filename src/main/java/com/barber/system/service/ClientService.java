package com.barber.system.service;

import com.barber.system.model.Client;
import com.barber.system.model.BarberShop;
import com.barber.system.repository.ClientRepository;
import com.barber.system.security.TenantContextService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ClientService {

    private final ClientRepository clientRepository;
    private final TenantContextService tenantContextService;

    @Autowired
    public ClientService(ClientRepository clientRepository, TenantContextService tenantContextService) {
        this.clientRepository = clientRepository;
        this.tenantContextService = tenantContextService;
    }

    @Transactional(readOnly = true)
    public List<Client> getAllClients() {
        return clientRepository.findByBarberShop(tenantContextService.getCurrentBarberShop());
    }

    @Transactional(readOnly = true)
    public Optional<Client> getClientByPhone(String phone) {
        return clientRepository.findByBarberShopAndPhone(tenantContextService.getCurrentBarberShop(), phone);
    }

    @Transactional(readOnly = true)
    public long getClientLoyalty(Long clientId) {
        return clientRepository.findById(clientId)
                .map(client -> (long) client.getAppointments().size())
                .orElse(0L);
    }

    @Transactional
    public Client saveClient(Client client) {
        client.setBarberShop(tenantContextService.getCurrentBarberShop());
        return clientRepository.save(client);
    }
}
