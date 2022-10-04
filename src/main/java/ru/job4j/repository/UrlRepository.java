package ru.job4j.repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import ru.job4j.model.Registration;
import ru.job4j.model.URL;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

public interface UrlRepository extends CrudRepository<URL, Integer> {

    Optional<URL> findByUrl(String url);

    Optional<URL> findByKey(String key);

    @Modifying
    @Transactional
    @Query("UPDATE URL u SET u.callsNumber = u.callsNumber + 1 WHERE u.key = :key")
    int increaseCallsNumberByKey(String key);

    List<URL> findAllByOwner(Registration owner);

}
