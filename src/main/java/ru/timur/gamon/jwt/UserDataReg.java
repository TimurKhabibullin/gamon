package ru.timur.gamon.jwt;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class UserDataReg implements UserData {
    @JsonProperty("username")
    private String username;
    @JsonProperty("email")
    private String email;

    @JsonProperty("password")
    private String password;
}
