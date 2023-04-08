package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.Generated;
import lombok.NonNull;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Data
public class Film {
    @Generated
    protected int id;

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
    @Size(min = 1, max = 1200)
    protected Integer duration;                     //Minutes

}
