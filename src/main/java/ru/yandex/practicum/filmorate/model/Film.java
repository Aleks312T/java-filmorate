package ru.yandex.practicum.filmorate.model;

import lombok.*;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
public class Film {
    int id;

    @NonNull
    String name;

    @NonNull
    String description;

    @NonNull
    LocalDate releaseDate;

    @NonNull
    Integer duration;

}
