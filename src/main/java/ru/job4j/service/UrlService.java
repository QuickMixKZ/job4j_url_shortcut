package ru.job4j.service;

import net.bytebuddy.utility.RandomString;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
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
    private final int stringLength;

    public UrlService(UrlRepository urlRepository,
                      RegistrationService registrationService,
                      @Value("${string.length}") int stringLength) {
        this.urlRepository = urlRepository;
        this.registrationService = registrationService;
        this.stringLength = stringLength;
    }

    public UrlDTO convert(UrlDTO urlDTO) {
        Registration currentUser =
                registrationService.findByLogin(
                        SecurityContextHolder
                                .getContext()
                                .getAuthentication()
                                .getName()
                ).orElseThrow(() -> new ResponseStatusException(HttpStatus.FORBIDDEN, "Not authorized"));
        urlDTO.setKey(RandomString.make(stringLength));
        try {
            urlRepository.save(
                    new URL(
                            urlDTO.getUrl(),
                            urlDTO.getKey(),
                            currentUser
                    )
            );
        } catch (DataIntegrityViolationException e) {
            Optional<URL> urlDb = urlRepository.findByUrl(urlDTO.getUrl());
            urlDTO.setKey(urlDb.get().getKey());
        }
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
