package ru.otus.service;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
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
        if (StringUtils.isNotBlank(clientDto.getStreet())) {
            client.setAddress(new Address(clientDto.getStreet()));
        }
        for (String phone : clientDto.getPhones()) {
            if (StringUtils.isNotBlank(phone)) {
                client.addPhone(new Phone(phone));
            }
        }
        return client;
    }

    public ClientDto convert(Client client) {
        ClientDto clientDto = new ClientDto();
        clientDto.setId(client.getId());
        clientDto.setName(client.getName());
        if (client.getAddress() != null && StringUtils.isNotBlank(client.getAddress().getStreet())) {
            clientDto.setStreet(client.getAddress().getStreet());
        }
        client.getPhones().forEach(item -> clientDto.getPhones().add(item.getNumber()));
        clientDto.getPhones().sort(ObjectUtils::compare);
        return clientDto;
    }
}
