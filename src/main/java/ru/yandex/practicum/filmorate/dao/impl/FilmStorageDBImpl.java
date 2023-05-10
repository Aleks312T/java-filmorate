package ru.yandex.practicum.filmorate.dao.impl;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.interf.FilmStorageDB;
import ru.yandex.practicum.filmorate.exception.ObjectDoesntExistException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.time.LocalDate;
import java.util.*;

@RequiredArgsConstructor
@Component
public class FilmStorageDBImpl implements FilmStorageDB {

    private static final Logger log = LoggerFactory.getLogger(FilmStorageDBImpl.class);

    private final JdbcTemplate jdbcTemplate;

    public Collection<Film> findAll() {
        log.trace("Получение всех фильмов");
        String sql = "SELECT * FROM films f " +
                     "JOIN rating r ON f.ratingId = r.ratingId";
                     //"";
        SqlRowSet srs = jdbcTemplate.queryForRowSet(sql);
        List<Film> films = new ArrayList<>();
        while (srs.next()) {
            films.add(filmMap(srs));
        }
        return films;
    }

    public Film createFilm(Film film) {
        log.trace("Добавление нового фильма");
        String sql = "INSERT INTO films " +
                "(filmName,description,releaseDate,duration,ratingId) " +
                "VALUES (?,?,?,?,?)";
        jdbcTemplate.update(sql,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getMpa().getId());

        film.setId(getNewFilmId());

        //TODO: проверить потом жанры
        if (film.getGenres() != null) {
            connectFilmGenre(film);
        } else {
            film.setGenres(new HashSet<>());
        }

        //return getFilm(film.getId());
        return film;
    }

    public Film updateFilm(Film film) {
        log.trace("Изменение фильма");
        String sql = "UPDATE films " +
                     "SET filmName = ?, description = ?, releaseDate = ?, duration = ?, ratingId  = ? " +
                     "WHERE filmId = ?";
        jdbcTemplate.update(sql,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getMpa().getId(),
                film.getId());
        return film;
    }

    public Film getFilm(Integer filmId) {
        log.trace("Получение фильма");
        String sql = "SELECT * FROM films WHERE filmId=?";
        SqlRowSet srs = jdbcTemplate.queryForRowSet(sql, filmId);
        if (srs.next()) {
            return filmMap(srs);
        } else {
            String errorMessage = "Фильма с Id " + filmId + " нет.";
            log.warn(errorMessage);
            throw new ObjectDoesntExistException(errorMessage);
        }
    }

    public Film addLike(Integer filmId, Integer userId) {
        return null;
    }

    public Film removeLike(Integer filmId, Integer userId) {
        return null;
    }

    public Collection<Film> getPopularFilm(Integer count) {
        return null;
    }

    public Collection<Integer> returnLikes(Integer filmId) {

        return null;
    }

    private int getNewFilmId() {
        String sql = "SELECT filmId \n" +
                          "FROM films f \n" +
                          "ORDER BY filmId DESC \n" +
                          "LIMIT 1";

        List<Integer> id = jdbcTemplate.query(sql, (rs, rowNum) -> rs.getInt("filmId"));
        return id.get(0);
    }

    private void connectFilmGenre(Film film) {

    }

    private static Film filmMap(SqlRowSet srs) {
        int filmId = srs.getInt("filmId");
        String filmName = srs.getString("filmName");
        String description = srs.getString("description");
        LocalDate releaseDate = Objects.requireNonNull(srs.getTimestamp("releaseDate"))
                .toLocalDateTime().toLocalDate();
        int duration = srs.getInt("duration");
        int mpaId = srs.getInt("ratingId");

        //TODO: сделать mpa
        Mpa mpa = Mpa.builder()
                .id(mpaId)
                //.name(mpaName)
                .build();
        //Set<Genre> genres = getGenres(id);
        Set<Genre> genres = new HashSet<>();
        return Film.builder()
                .id(filmId)
                .name(filmName)
                .description(description)
                .duration(duration)
                .mpa(mpa)
                .genres(genres)
                .releaseDate(releaseDate)
                .build();
    }
}
