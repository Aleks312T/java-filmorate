package ru.yandex.practicum.model;

import lombok.Data;

import java.time.Instant;
import java.util.Date;

@Data
public class Film {
    int id;
    String name;
    String description;
    Date releaseDate;
    Instant duration;
}
