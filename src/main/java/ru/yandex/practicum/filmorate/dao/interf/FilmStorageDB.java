package ru.yandex.practicum.filmorate.dao.interf;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;

//TODO: нужно будет доделать
public interface FilmStorageDB {
    public Collection<Film> findAll();

    public Film createFilm(Film film);

    public Film updateFilm(Film film);

    public Film getFilm(Integer filmId);

    public Film addLike(Integer filmId, Integer userId);

    public Film removeLike(Integer filmId, Integer userId);

    public Collection<Film> getPopularFilm(Integer count);

    public Collection<Integer> returnLikes(Integer filmId);

}