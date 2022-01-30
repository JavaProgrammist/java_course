package ru.otus.repository;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import ru.otus.model.Client;

import java.util.List;

public interface ClientRepo extends CrudRepository<Client, Long> {

    @Query(value = "select c.id client_id, " +
            "c.name client_name, " +
            "a.id address_id, " +
            "a.street address_street, " +
            "p.id phone_id, " +
            "p.number phone_number " +
            "from client c " +
            "left outer join address a on c.id=a.client_id " +
            "left outer join phone p on c.id=p.client_id",
    resultSetExtractorClass = ClientResultSetExtractor.class)
    List<Client> findAll();
}
