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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.timur.gamon.models.Booking;
import java.io.IOException;
import java.time.*;
import java.util.*;

@Service
@Transactional(readOnly = true)
public class BookingService {


    @Transactional
    public boolean create(Booking booking,int computerId, String jwtToken) {
        String url = "http://localhost:8080/booking/create/" + computerId;
        ObjectMapper objectMapper = new ObjectMapper();
        int statusCode = 0;
        // Сериализация объекта UserData в JSON-строку
        String json = null;
        try {
            json = objectMapper.writeValueAsString(booking);
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
        if (statusCode == 400){
            return false;
        }

        return true;
    }


//    @Transactional
//    public List<Booking> getBookingsByDate(long date){
//        deleteOverdueBookings();
//        return bookingRepository.findByBookingDateForDb(date);
//    }

    @Transactional
    public List<Booking> findAll(String jwtToken) {
        List<Booking> bookings = null;
        String url = "http://localhost:8080/booking/findAll";
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
                bookings = objectMapper.readValue(json, new TypeReference<>() {
                });
            } catch (IOException e) {
                e.printStackTrace();
            }

            // Используем полученный объект по своему усмотрению
        } catch (Exception e) {
            e.printStackTrace();
        }

        assert bookings != null;
        for (Booking booking : bookings){
            long test_timestamp = booking.getBookingDateForDb();
            LocalDateTime triggerTime =
                    LocalDateTime.ofInstant(Instant.ofEpochMilli(test_timestamp),
                            TimeZone.getDefault().toZoneId());
            booking.setBookingDate(triggerTime.toString().split("T")[0]);
        }


        return bookings;
    }

    @Transactional
    public List<Booking> findAllByComputer(int id,String jwtToken){

        List<Booking> bookings = null;
        String url = "http://localhost:8080/booking/findAllByComputer/" + id;
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
                bookings = objectMapper.readValue(json, new TypeReference<>() {
                });
            } catch (IOException e) {
                e.printStackTrace();
            }

            // Используем полученный объект по своему усмотрению
        } catch (Exception e) {
            e.printStackTrace();
        }

        assert bookings != null;
        for (Booking booking : bookings){
            long test_timestamp = booking.getBookingDateForDb();
            LocalDateTime triggerTime =
                    LocalDateTime.ofInstant(Instant.ofEpochMilli(test_timestamp),
                            TimeZone.getDefault().toZoneId());
            booking.setBookingDate(triggerTime.toString().split("T")[0]);
        }
        return bookings;
    }

    @Transactional
    public List<Booking> findBookingByPersonId(String jwtToken){
        List<Booking> bookings = null;
        String url = "http://localhost:8080/booking/findBookingByPersonId";
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
                bookings = objectMapper.readValue(json, new TypeReference<>() {
                });
            } catch (IOException e) {
                e.printStackTrace();
            }

            // Используем полученный объект по своему усмотрению
        } catch (Exception e) {
            e.printStackTrace();
        }

        assert bookings != null;
        for (Booking booking : bookings){
            long test_timestamp = booking.getBookingDateForDb();
            LocalDateTime triggerTime =
                    LocalDateTime.ofInstant(Instant.ofEpochMilli(test_timestamp),
                            TimeZone.getDefault().toZoneId());
            booking.setBookingDate(triggerTime.toString().split("T")[0]);
        }
        return bookings;
    }

    public Booking findById(int id,String jwtToken) {
        Booking booking = null;
        String url = "http://localhost:8080/booking/findById/" + id;
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
                booking = objectMapper.readValue(json, new TypeReference<>() {
                });
            } catch (IOException e) {
                e.printStackTrace();
            }

            // Используем полученный объект по своему усмотрению
        } catch (Exception e) {
            e.printStackTrace();
        }

        assert booking != null;
        long timestamp = booking.getBookingDateForDb();
        LocalDateTime triggerTime =
                LocalDateTime.ofInstant(Instant.ofEpochMilli(timestamp),
                        TimeZone.getDefault().toZoneId());
        booking.setBookingDate(triggerTime.toString());
        return booking;
    }

    @Transactional
    public void delete(int id,String jwtToken) {
        String url = "http://localhost:8080/booking/delete/" + id;
        // Создание объекта CloseableHttpClient
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            // Создание объекта HttpPost с URL-адресом сервера
            HttpPost httpPost = new HttpPost(url);
            httpPost.setHeader(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken);
            // Выполнение запроса и получение ответа от сервера
            HttpResponse response = httpClient.execute(httpPost);
            System.out.println(response);
        } catch (IOException e) {
            throw new RuntimeException();
        }
    }
}
