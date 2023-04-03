package ru.yandex.practicum.model;

import lombok.Data;
import lombok.NonNull;

import java.time.LocalDate;
import java.util.Date;

@Data
public class User {
    @NonNull
    int id;

    @NonNull
    String email;

    @NonNull
    String login;

    @NonNull
    String name;
    LocalDate birthday;
}
