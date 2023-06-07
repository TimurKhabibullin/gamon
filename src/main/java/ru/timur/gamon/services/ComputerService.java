package ru.timur.gamon.services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpHeaders;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.timur.gamon.models.Computer;

import java.io.IOException;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class ComputerService {
    public List<Computer> showAll(String jwtToken){
        List<Computer> computers = null;
        String url = "http://localhost:8080/computer/showAll";
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpGet request = new HttpGet(url);

            // Устанавливаем заголовок Authorization с JWT-токеном
            request.setHeader(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken);

            // Выполняем запрос
            CloseableHttpResponse response = httpClient.execute(request);

            // Получаем ответ в виде строки
            String json = EntityUtils.toString(response.getEntity());

            // Десериализуем JSON-объект с помощью Jackson
            ObjectMapper objectMapper = new ObjectMapper();
            try {
                computers = objectMapper.readValue(json, new TypeReference<>() {
                });
            } catch (IOException e) {
                e.printStackTrace();
            }

            // Используем полученный объект по своему усмотрению
        } catch (Exception e) {
            e.printStackTrace();
        }



        return computers;
    }

    public Computer findById(int id, String jwtToken){
        String url = "http://localhost:8080/computer/findById/" + id;
        Computer responseObject = null;
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
            responseObject = objectMapper.readValue(responseBody, Computer.class);

            // Используем полученный объект по своему усмотрению
        } catch (Exception e) {
            e.printStackTrace();
        }

        return responseObject;
    }
}
