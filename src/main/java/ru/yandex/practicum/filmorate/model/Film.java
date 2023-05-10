package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NonNull;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.Set;

@Data
@Builder
public class Film {
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

    private Set<Genre> genres;
    @NotNull
    private Mpa mpa;

    @Getter
    Set<Integer> likes;
    protected Integer id;

}
