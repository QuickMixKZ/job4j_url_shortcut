package ru.job4j.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.job4j.dto.UserDTO;
import ru.job4j.dto.SiteDTO;
import ru.job4j.service.RegistrationService;

import javax.validation.Valid;

@RestController
public class RegistrationController {

    private final RegistrationService registrationService;

    public RegistrationController(RegistrationService registrationService) {
        this.registrationService = registrationService;
    }

    @PostMapping("/registration")
    public ResponseEntity<UserDTO> registration(@RequestBody @Valid SiteDTO siteDTO) {
        return new ResponseEntity<>(
                registrationService.register(siteDTO),
                HttpStatus.CREATED
        );
    }

}
