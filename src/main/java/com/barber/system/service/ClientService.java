package com.barber.system.service;

import com.barber.system.model.Client;
import com.barber.system.repository.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ClientService {

    private final ClientRepository clientRepository;

    @Autowired
    public ClientService(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    @Transactional(readOnly = true)
    public List<Client> getAllClients() {
        return clientRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<Client> getClientByPhone(String phone) {
        return clientRepository.findByPhone(phone);
    }

    @Transactional(readOnly = true)
    public long getClientLoyalty(Long clientId) {
        return clientRepository.findById(clientId)
                .map(client -> (long) client.getAppointments().size())
                .orElse(0L);
    }

    @Transactional
    public Client saveClient(Client client) {
        return clientRepository.save(client);
    }
}
