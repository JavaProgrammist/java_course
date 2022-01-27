package ru.otus.service;

import org.springframework.stereotype.Service;
import ru.otus.model.Address;
import ru.otus.model.Client;
import ru.otus.model.ClientDto;
import ru.otus.model.Phone;

@Service
public class ClientConverter {

    public Client convert(ClientDto clientDto) {
        Client client = new Client();
        client.setName(clientDto.getName());
        client.setAddress(new Address(clientDto.getStreet()));

        for (String phone : clientDto.getPhones()) {
            client.addPhone(new Phone(phone));
        }
        return client;
    }

    public ClientDto convert(Client client) {
        ClientDto clientDto = new ClientDto();
        clientDto.setId(client.getId());
        clientDto.setName(client.getName());
        clientDto.setStreet(client.getAddress().getStreet());
        client.getPhones().forEach(item -> clientDto.getPhones().add(item.getNumber()));
        return clientDto;
    }
}
