package ru.job4j.service;

import net.bytebuddy.utility.RandomString;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.job4j.dto.UrlDTO;
import ru.job4j.model.Registration;
import ru.job4j.model.URL;
import ru.job4j.repository.UrlRepository;

import java.util.List;
import java.util.Optional;

@Service
public class UrlService {

    private final UrlRepository urlRepository;
    private final RegistrationService registrationService;

    public UrlService(UrlRepository urlRepository, RegistrationService registrationService) {
        this.urlRepository = urlRepository;
        this.registrationService = registrationService;
    }

    public UrlDTO convert(UrlDTO urlDTO) {
        Optional<URL> urlDb = urlRepository.findByUrl(urlDTO.getUrl());
        urlDb.ifPresentOrElse(
                url -> urlDTO.setKey(url.getKey()),
                () -> {
                    urlDTO.setKey(RandomString.make(8));
                    Registration currentUser =
                            registrationService.findByLogin(
                                    SecurityContextHolder
                                            .getContext()
                                            .getAuthentication()
                                            .getName()
                            ).orElseThrow(() -> new ResponseStatusException(HttpStatus.FORBIDDEN, "Not authorized"));
                    urlRepository.save(
                            new URL(
                                    urlDTO.getUrl(),
                                    urlDTO.getKey(),
                                    currentUser
                            )
                    );

                }
        );
        return urlDTO;
    }

    public Optional<URL> findByKey(String key) {
        return urlRepository.findByKey(key);
    }

    public int increaseCallsNumberByKey(String key) {
        return urlRepository.increaseCallsNumberByKey(key);
    }

    public List<URL> findAllByOwner(Registration owner) {
        return urlRepository.findAllByOwner(owner);
    }

}
