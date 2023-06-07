package ru.timur.gamon.jwt;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class TokenResponse {
    @JsonProperty("jwt-token")
    private String token;
}