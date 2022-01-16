package ru.otus.model;


import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Table(name = "client", schema = "public")
public class Client implements Cloneable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @OneToOne(mappedBy = "client", cascade = CascadeType.ALL, orphanRemoval = true)
    private Address address;

    @Fetch(FetchMode.SUBSELECT)
    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL, orphanRemoval = true,
            fetch = FetchType.EAGER)
    private Set<Phone> phones;

    public Client() {
    }

    public Client(String name) {
        this.id = null;
        this.name = name;
    }

    public Client(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Client(Long id, String name, String street, String phone) {
        this.id = id;
        this.name = name;
        this.setAddress(new Address(street, this));
        this.getPhones().add(new Phone(phone, this));
    }

    @Override
    public Client clone() {
        Client clone = new Client(this.id, this.name);
        if (this.phones != null) {
            Set<Phone> phones = this.phones.stream().map(item -> new Phone(item, clone))
                    .collect(Collectors.toSet());
            clone.setPhones(phones);
        }
        if (this.getAddress() != null) {
            clone.setAddress(new Address(this.getAddress(), clone));
        }
        return clone;
    }

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

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        address.setClient(this);
        this.address = address;
    }

    public void clearAddress() {
        if (address != null) {
            address.setClient(null);
            address = null;
        }
    }

    public Set<Phone> getPhones() {
        if (phones == null) {
            phones = new HashSet<>();
        }
        return phones;
    }

    public void setPhones(Set<Phone> phones) {
        this.phones = phones;
    }

    public void addPhone(Phone phone) {
        if (this.phones == null) {
            this.phones = new HashSet<>();
        }
        this.phones.add(phone);
    }

    public void clearPhones() {
        if (phones != null) {
            phones.forEach(item -> item.setClient(null));
            phones.clear();
        }
    }

    @Override
    public String toString() {
        return "Client{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
