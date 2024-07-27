package ru.skillbox.model.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class CountryResponse implements Serializable {
    private int id;
    private String title;
}
