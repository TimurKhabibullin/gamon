package ru.timur.gamon.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Service;
import ru.timur.gamon.jwt.JwtTokenClient;
import ru.timur.gamon.jwt.UserDataLog;
import ru.timur.gamon.models.Person;

import java.io.IOException;

@Service
public class PeopleService {


    public Person loadUser(String jwtToken) {
        // Создание GET-запроса
        String url = "http://localhost:8080/people/findPerson";
        Person responseObject = null;
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpGet request = new HttpGet(url);

            // Устанавливаем заголовок Authorization с JWT-токеном
            request.setHeader(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken);

            // Выполняем запрос
            CloseableHttpResponse response = httpClient.execute(request);

            // Получаем ответ в виде строки
            String responseBody = EntityUtils.toString(response.getEntity());
            System.out.println(responseBody);

            // Десериализуем JSON-объект с помощью Jackson
            ObjectMapper objectMapper = new ObjectMapper();
            responseObject = objectMapper.readValue(responseBody, Person.class);

            // Используем полученный объект по своему усмотрению
        } catch (Exception e) {
            e.printStackTrace();
        }

        return responseObject;
    }

    public String login(String username, String password) {
        UserDataLog userData = new UserDataLog();
        userData.setUsername(username);
        userData.setPassword(password);

        JwtTokenClient jwtTokenClient = new JwtTokenClient("http://localhost:8080/auth/login");
        String token;
        try {
            token = jwtTokenClient.getToken(userData);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        System.out.println(token);

        return token;
    }

    public void edit(Person personToBeUpdated, String jwtToken) {

        System.out.println("Update Person   " + personToBeUpdated);
        String url = "http://localhost:8080/people/edit";

        ObjectMapper objectMapper = new ObjectMapper();
        int statusCode = 0;
        // Сериализация объекта UserData в JSON-строку
        String json = null;
        try {
            json = objectMapper.writeValueAsString(personToBeUpdated);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        // Создание объекта CloseableHttpClient
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            // Создание объекта HttpPost с URL-адресом сервера
            HttpPost httpPost = new HttpPost(url);
            httpPost.setHeader(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken);


            // Установка JSON-строки в качестве сущности запроса
            StringEntity entity = new StringEntity(json, ContentType.APPLICATION_JSON);
            httpPost.setEntity(entity);

            // Выполнение запроса и получение ответа от сервера
            HttpResponse response = httpClient.execute(httpPost);
            statusCode = response.getStatusLine().getStatusCode();
            System.out.println(statusCode);

            System.out.println(response);
        } catch (IOException e) {
            throw new RuntimeException();
        }
    }
}
