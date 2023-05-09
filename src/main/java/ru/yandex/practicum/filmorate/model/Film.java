package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@Builder
public class Film {
    @NotBlank
    @NonNull
    //Оставил NonNull, чтобы name требовался в конструкторе
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

    Set<Integer> likes = new HashSet<>();
    protected Integer id;

    //Решил отдельно сделать нужный мне конструктор
    public Film(String name, String description, LocalDate releaseDate, Integer duration) {
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
    }

    public Set<Integer> getLikes() {
        return likes;
    }
}
