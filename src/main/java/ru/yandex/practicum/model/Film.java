package ru.yandex.practicum.model;

import lombok.Data;
import lombok.NonNull;

import java.time.Duration;
import java.time.LocalDate;

@Data
public class Film {
    @NonNull
    int id;

    @NonNull
    String name;

    String description;

    @NonNull
    LocalDate releaseDate;

    @NonNull
    Duration duration;
}
