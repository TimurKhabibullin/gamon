package ru.timur.gamon.jwt;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class UserDataLog implements UserData {
    @JsonProperty("username")
    private String username;

    @JsonProperty("password")
    private String password;
}
