package ru.otus.crm.model;

import javax.persistence.*;

@Entity
@Table(name = "address", schema = "public")
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "street")
    private String street;

    @OneToOne
    @JoinColumn(name = "client_id")
    private Client client;

    public Address() {
    }

    public Address(String street) {
        this.street = street;
    }

    public Address(String street, Client client) {
        this.street = street;
        this.client = client;
    }

    public Address(Address source, Client client) {
        this.id = source.id;
        this.street = source.street;
        this.client = client;
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

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }
}
