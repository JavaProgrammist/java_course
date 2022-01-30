package ru.otus.repository;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import ru.otus.model.Address;
import ru.otus.model.Client;
import ru.otus.model.Phone;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

public class ClientResultSetExtractor implements ResultSetExtractor<List<Client>> {
    @Override
    public List<Client> extractData(ResultSet rs) throws SQLException, DataAccessException {
        Map<Long, Client> clientMap = new HashMap<>();
        while (rs.next()) {
            var clientId = rs.getLong("client_id");
            Client client = clientMap.get(clientId);
            if (client == null) {
                client = new Client(clientId, rs.getString("client_name"));
                clientMap.put(clientId, client);
            }
            addAddressData(client, rs);
            addPhoneData(client, rs);
        }
        return clientMap.values().stream()
                .sorted(Comparator.comparing(Client::getName))
                .collect(Collectors.toList());
    }

    private void addAddressData(Client client, ResultSet rs) throws SQLException {
        Long addressId = (Long) rs.getObject("address_id");
        if (addressId != null) {
            Address address = new Address(addressId, rs.getString("address_street"), client.getId());
            client.setAddress(address);
        }
    }

    private void addPhoneData(Client client, ResultSet rs) throws SQLException {
        Long phoneId = (Long) rs.getObject("phone_id");
        if (phoneId != null) {
            Phone phone = new Phone(phoneId, rs.getString("phone_number"), client.getId());
            client.getPhones().add(phone);
        }
    }
}
