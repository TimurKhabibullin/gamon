package ru.timur.gamon.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;


@Data
public class Computer {
    @JsonProperty("id")
    private int id;
}
