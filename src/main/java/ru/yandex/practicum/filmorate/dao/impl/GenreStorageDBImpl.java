package ru.yandex.practicum.filmorate.dao.impl;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.interf.GenreStorageDB;
import ru.yandex.practicum.filmorate.exception.ObjectDoesntExistException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Component
public class GenreStorageDBImpl implements GenreStorageDB {
    private static final Logger log = LoggerFactory.getLogger(GenreStorageDBImpl.class);

    private final JdbcTemplate jdbcTemplate;

    @Override
    public Genre getGenre(Integer genreId) {
        log.trace("Получение одного жанра");
        String sql = "SELECT * FROM genre WHERE genreId = ?";
        SqlRowSet srs = jdbcTemplate.queryForRowSet(sql, genreId);
        if (srs.next()) {
            return new Genre(genreId, srs.getString("genreName"));
        } else {
            String errorMessage = "Жанра с Id " + genreId + " нет.";
            log.warn(errorMessage);
            throw new ObjectDoesntExistException(errorMessage);
        }
    }

    @Override
    public List<Genre> getAllGenres() {
        log.trace("Получение всех жанров");
        List<Genre> genres = new ArrayList<>();
        String sql = "SELECT * FROM genre ";
        SqlRowSet srs = jdbcTemplate.queryForRowSet(sql);
        while (srs.next()) {
            genres.add(new Genre(srs.getInt("genreId"), srs.getString("genreName")));
        }
        return genres;
    }
}
