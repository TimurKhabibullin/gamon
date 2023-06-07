package ru.timur.gamon.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.timur.gamon.models.Computer;
import ru.timur.gamon.models.News;

import java.io.IOException;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class NewsService {

    public List<News> getNews(){
        List<News> news = null;
        String url = "http://localhost:8080/admin/showAll";
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpGet request = new HttpGet(url);

            // Выполняем запрос
            CloseableHttpResponse response = httpClient.execute(request);

            // Получаем ответ в виде строки
            String json = EntityUtils.toString(response.getEntity());

            // Десериализуем JSON-объект с помощью Jackson
            ObjectMapper objectMapper = new ObjectMapper();
            try {
                news = objectMapper.readValue(json, new TypeReference<>() {
                });
            } catch (IOException e) {
                e.printStackTrace();
            }

            // Используем полученный объект по своему усмотрению
        } catch (Exception e) {
            e.printStackTrace();
        }



        return news;
    }

    public News getOne(int id){
        String url = "http://localhost:8080/admin/findById/" + id;
        News responseObject = null;
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpGet request = new HttpGet(url);

            // Выполняем запрос
            CloseableHttpResponse response = httpClient.execute(request);

            // Получаем ответ в виде строки
            String responseBody = EntityUtils.toString(response.getEntity());
            System.out.println(responseBody);

            // Десериализуем JSON-объект с помощью Jackson
            ObjectMapper objectMapper = new ObjectMapper();
            responseObject = objectMapper.readValue(responseBody, News.class);

            // Используем полученный объект по своему усмотрению
        } catch (Exception e) {
            e.printStackTrace();
        }

        return responseObject;
    }

    @Transactional
    public void createNews(News news, String jwtToken){

        System.out.println(news);
        String url = "http://localhost:8080/admin/create";
        ObjectMapper objectMapper = new ObjectMapper();
        int statusCode = 0;
        // Сериализация объекта UserData в JSON-строку
        String json = null;
        try {
            json = objectMapper.writeValueAsString(news);
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
