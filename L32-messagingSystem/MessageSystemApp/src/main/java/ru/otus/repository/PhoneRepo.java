package ru.otus.repository;

import org.springframework.data.repository.CrudRepository;
import ru.otus.model.Phone;

public interface PhoneRepo extends CrudRepository<Phone, Long> {
}
