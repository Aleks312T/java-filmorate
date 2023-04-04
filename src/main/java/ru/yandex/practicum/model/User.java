package ru.yandex.practicum.model;

import lombok.*;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
public class User {
    int id;

    @NonNull
    String email;

    @NonNull
    String login;

    String name;

    @NonNull
    LocalDate birthday;
}
