package ru.job4j.model;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "registrations")
@Data
public class Registration {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String site;
    private String login;
    private String password;

    public Registration(String site, String login, String password) {
        this.site = site;
        this.login = login;
        this.password = password;
    }

    public Registration() {

    }
}
