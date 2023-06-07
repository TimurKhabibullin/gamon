package ru.timur.gamon.services;

import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.timur.gamon.jwt.JwtTokenClient;
import ru.timur.gamon.jwt.UserDataReg;
import ru.timur.gamon.models.Person;

@Service
public class RegistrationService {

    @SneakyThrows
    @Transactional
    public String register(Person person) {

        UserDataReg userData = new UserDataReg();
        userData.setUsername(person.getUsername());
        userData.setEmail(person.getEmail());
        userData.setPassword(person.getPassword());

        JwtTokenClient jwtTokenClient = new JwtTokenClient("http://localhost:8080/auth/registration");
        String token = jwtTokenClient.getToken(userData);

        System.out.println(token);

        return token;
    }
}