package ru.yandex.practicum.filmorate.storage.dao.film.interf;

import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;

public interface GenreStorageDB {

    Genre getGenre(Integer genreId);

    List<Genre> getAllGenres();
}
