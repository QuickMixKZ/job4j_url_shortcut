package ru.job4j.service;

import net.bytebuddy.utility.RandomString;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import ru.job4j.dto.SiteDTO;
import ru.job4j.dto.UserDTO;
import ru.job4j.model.Registration;
import ru.job4j.repository.RegistrationRepository;

import java.util.Optional;

@Service
public class RegistrationService {

    private final RegistrationRepository registrationRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final int stringLength;

    public RegistrationService(RegistrationRepository registrationRepository,
                               BCryptPasswordEncoder passwordEncoder,
                               @Value("${string.length}") int stringLength) {
        this.registrationRepository = registrationRepository;
        this.passwordEncoder = passwordEncoder;
        this.stringLength = stringLength;
    }

    public UserDTO register(SiteDTO siteDTO) {
        UserDTO result = new UserDTO();
        result.setLogin(RandomString.make(stringLength));
        result.setPassword(RandomString.make(stringLength));
        try {
            registrationRepository.save(
                    new Registration(
                            siteDTO.getSite(),
                            result.getLogin(),
                            passwordEncoder.encode(result.getPassword())
                    )
            );
            result.setRegistration(true);
        } catch (DataIntegrityViolationException e) {
            result = new UserDTO();
            result.setRegistration(false);
        }
        return result;
    }

    public Optional<Registration> findByLogin(String login) {
        return registrationRepository.findByLogin(login);
    }

}
