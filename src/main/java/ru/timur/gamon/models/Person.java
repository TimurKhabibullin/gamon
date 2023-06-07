package ru.timur.gamon.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class Person {
    @JsonProperty("id")
    private int id;
    @NotEmpty(message = "This field should not be empty")
    @Size(min = 3,max = 100,message = "Name should be greater than 3 characters and smaller than 100 characters")
    @JsonProperty("username")
    private String username;
    @Email
    @NotEmpty(message = "This field should not be empty")
    @JsonProperty("email")
    private String email;
    @NotEmpty(message = "This field should not be empty")
    @JsonProperty("password")
    private String password;
    @JsonProperty("role")
    private String role;

    @JsonProperty("balance")
    private int balance;
}
