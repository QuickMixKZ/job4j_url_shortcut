package ru.job4j.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.job4j.dto.StatisticDTO;
import ru.job4j.dto.UrlDTO;
import ru.job4j.model.Registration;
import ru.job4j.model.URL;
import ru.job4j.service.RegistrationService;
import ru.job4j.service.UrlService;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
public class URLController {

    private final UrlService urlService;
    private final RegistrationService registrationService;

    public URLController(UrlService urlService, RegistrationService registrationService) {
        this.urlService = urlService;
        this.registrationService = registrationService;
    }

    @PostMapping("/convert")
    public ResponseEntity<UrlDTO> convert(@RequestBody @Valid UrlDTO urlDTO) {
        return new ResponseEntity<>(
                urlService.convert(urlDTO),
                HttpStatus.ACCEPTED
        );
    }

    @GetMapping("/redirect/{urlKey}")
    public ResponseEntity<String> redirect(@PathVariable String urlKey) {
        Optional<URL> result = urlService.findByKey(urlKey);
        if (result.isEmpty()) {
            throw new IllegalArgumentException(String.format("URL key \"%s\" not found!", urlKey));
        }
        urlService.increaseCallsNumberByKey(urlKey);
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set("REDIRECT URL",
                result.get().getUrl());
        return new ResponseEntity<>(
                responseHeaders,
                HttpStatus.FOUND
        );
    }

    @GetMapping("/statistic")
    public ResponseEntity<List<StatisticDTO>> statistic() {
        Registration currentUser =
                registrationService.findByLogin(
                        SecurityContextHolder
                                .getContext()
                                .getAuthentication()
                                .getName()
                ).orElseThrow(() -> new ResponseStatusException(HttpStatus.FORBIDDEN, "Not authorized"));
        List<StatisticDTO> statistic = new ArrayList<>();
        urlService.findAllByOwner(currentUser).
                forEach(url -> statistic.add(new StatisticDTO(url.getUrl(), url.getCallsNumber())));
        return new ResponseEntity<>(statistic, HttpStatus.OK);
    }
}
