package ru.job4j.dto;

import lombok.Data;

@Data
public class UserDTO {

    private boolean registration;
    private String login;
    private String password;

}
