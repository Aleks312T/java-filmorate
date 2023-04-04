package ru.yandex.practicum.model;

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

    String description;

    @NonNull
    LocalDate releaseDate;

    @NonNull
    Integer duration;

}
