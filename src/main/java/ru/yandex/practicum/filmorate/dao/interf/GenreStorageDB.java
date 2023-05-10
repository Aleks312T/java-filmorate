package ru.yandex.practicum.filmorate.dao.interf;

import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;

public interface GenreStorageDB {

    Genre getGenre(Integer genreId);

    List<Genre> getAllGenres();
}
