package ru.otus.service.db;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.otus.model.Client;
import ru.otus.repository.ClientRepo;
import ru.otus.sessionmanager.TransactionManager;

import java.util.List;
import java.util.Optional;

@Service
public class DBServiceClientImpl implements DBServiceClient {
    private static final Logger log = LoggerFactory.getLogger(DBServiceClientImpl.class);

    private final ClientRepo clientRepo;
    private final TransactionManager transactionManager;

    public DBServiceClientImpl(ClientRepo clientRepo, TransactionManager transactionManager) {
        this.clientRepo = clientRepo;
        this.transactionManager = transactionManager;
    }

    @Override
    public Client saveClient(Client client) {
        return transactionManager.doInTransaction(() -> {
            Client savedClient = clientRepo.save(client);
            log.info("saved client: {}", savedClient);
            return savedClient;
        });
    }

    @Override
    public Optional<Client> getClient(long id) {
        return transactionManager.doInReadOnlyTransaction(() -> {
            var clientOptional = clientRepo.findById(id);
            log.info("client: {}", clientOptional);
            return clientOptional;
        });
    }

    @Override
    public List<Client> findAll() {
        return transactionManager.doInReadOnlyTransaction(() -> {
        List<Client> clientList = clientRepo.findAll();
            log.info("clientList:{}", clientList);
            return clientList;
        });
    }
}
