package ru.timur.gamon.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import java.io.IOException;

public class JwtTokenClient {
    private static String SERVER_URL;

    public JwtTokenClient(String SERVER_URL){
        this.SERVER_URL = SERVER_URL;
    }

    public String getToken(UserData userData) throws IOException {
        // Создание объекта ObjectMapper для сериализации/десериализации JSON
        ObjectMapper objectMapper = new ObjectMapper();

        // Сериализация объекта UserData в JSON-строку
        String json = objectMapper.writeValueAsString(userData);

        // Создание объекта CloseableHttpClient
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            // Создание объекта HttpPost с URL-адресом сервера
            HttpPost httpPost = new HttpPost(SERVER_URL);

            // Установка JSON-строки в качестве сущности запроса
            StringEntity entity = new StringEntity(json, ContentType.APPLICATION_JSON);
            httpPost.setEntity(entity);

            // Выполнение запроса и получение ответа от сервера
            HttpResponse response = httpClient.execute(httpPost);

            // Получение содержимого ответа в виде строки
            HttpEntity responseEntity = response.getEntity();
            String responseBody = EntityUtils.toString(responseEntity);

            // Десериализация JSON-ответа в объект, содержащий JWT токен
            TokenResponse tokenResponse = objectMapper.readValue(responseBody, TokenResponse.class);

            // Возвращение JWT токена
            return tokenResponse.getToken();
        }
    }
}