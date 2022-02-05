package ru.otus.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;


@Table("address")
public class Address {

    @Id
    @Column("id")
    private Long id;

    @Column("street")
    private String street;

    @Column("client_id")
    private Long clientId;

    public Address() {
    }

    public Address(String street) {
        this.street = street;
    }

    public Address(String street, Long clientId) {
        this.street = street;
        this.clientId = clientId;
    }

    public Address(Long id, String street, Long clientId) {
        this.id = id;
        this.street = street;
        this.clientId = clientId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public Long getClientId() {
        return clientId;
    }

    public void setClientId(Long clientId) {
        this.clientId = clientId;
    }
}
