package ru.otus.model;

import java.util.ArrayList;
import java.util.List;

public class ClientDto {

    private Long id;
    private String name;
    private String street = "";
    private List<String> phones;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public List<String> getPhones() {
        if (phones == null) {
            phones = new ArrayList<>();
        }
        return phones;
    }

    public void setPhones(List<String> phones) {
        this.phones = phones;
    }
}
