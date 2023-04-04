package ru.yandex.practicum.filmorate.model;

import lombok.*;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
public class User {
    int id;

    @NonNull
    String login;

    String name;

    @NonNull
    String email;

    @NonNull
    LocalDate birthday;
}
