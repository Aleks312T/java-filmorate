package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.Generated;
import lombok.NonNull;

import javax.validation.constraints.*;
import java.time.LocalDate;

@Data
public class Film {
    protected Integer id;

    @NotBlank
    @NonNull
    protected String name;

    @NonNull
    @Size(min = 1, max = 200)
    protected String description;

    @NonNull
    @PastOrPresent
    protected LocalDate releaseDate;

    @NonNull
    @Min(1)
    @Max(1200)
    protected Integer duration;                     //Minutes

}
