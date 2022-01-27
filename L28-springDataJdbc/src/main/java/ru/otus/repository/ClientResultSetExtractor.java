package ru.otus.repository;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import ru.otus.model.Address;
import ru.otus.model.Client;
import ru.otus.model.Phone;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ClientResultSetExtractor implements ResultSetExtractor<List<Client>> {
    @Override
    public List<Client> extractData(ResultSet rs) throws SQLException, DataAccessException {
        var clientList = new ArrayList<Client>();
        Client prevClient = null;
        while (rs.next()) {
            var clientId = rs.getLong("client_id");
            Client client = null;
            if (prevClient == null || !prevClient.getId().equals(clientId)) {
                client = new Client(clientId, rs.getString("client_name"));
                clientList.add(client);
                prevClient = client;
            }
            Long addressId = (Long) rs.getObject("address_id");
            if (client != null && addressId != null) {
                Address address = new Address(addressId, rs.getString("address_street"), clientId);
                client.setAddress(address);
            }
            Long phoneId = (Long) rs.getObject("phone_id");
            if (phoneId != null) {
                Phone phone = new Phone(phoneId, rs.getString("phone_number"), clientId);
                prevClient.getPhones().add(phone);
            }
        }
        return clientList;
    }
}
