package ru.yandex.practicum.filmorate.storage.dao.film.interf;

import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.List;

public interface MpaStorageDB {

    public List<Mpa> getAllMpa();

    public Mpa getMpa(Integer genreId);
}
