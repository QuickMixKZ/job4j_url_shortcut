package ru.job4j.service;

import net.bytebuddy.utility.RandomString;
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

    public RegistrationService(RegistrationRepository registrationRepository,
                               BCryptPasswordEncoder passwordEncoder) {
        this.registrationRepository = registrationRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public UserDTO register(SiteDTO siteDTO) {
        UserDTO result = new UserDTO();
        Optional<Registration> registrationDb = registrationRepository.findBySite(siteDTO.getSite());
        if (registrationDb.isPresent()) {
            result.setRegistration(false);
        } else {
            result.setRegistration(true);
            String login = RandomString.make(8);
            String password = RandomString.make(8);
            result.setLogin(login);
            result.setPassword(password);
            registrationRepository.save(
                    new Registration(
                            siteDTO.getSite(),
                            result.getLogin(),
                            passwordEncoder.encode(result.getPassword())
                    )
            );
        }
        return result;
    }

    public Optional<Registration> findByLogin(String login) {
        return registrationRepository.findByLogin(login);
    }

}
