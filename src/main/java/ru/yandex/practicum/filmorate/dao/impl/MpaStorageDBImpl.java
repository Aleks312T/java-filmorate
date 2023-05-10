package ru.yandex.practicum.filmorate.dao.impl;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.interf.MpaStorageDB;
import ru.yandex.practicum.filmorate.exception.ObjectDoesntExistException;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Component
public class MpaStorageDBImpl implements MpaStorageDB {
    private static final Logger log = LoggerFactory.getLogger(UserStorageDBImpl.class);
    private final JdbcTemplate jdbcTemplate;

    @Override
    public Mpa getMpa(Integer mpaId) {
        log.trace("Получение одного рейтинга");
        String sql = "SELECT * FROM rating WHERE ratingId = ?";
        SqlRowSet srs = jdbcTemplate.queryForRowSet(sql, mpaId);
        if (srs.next()) {
            return new Mpa(mpaId, srs.getString("ratingName"));
        } else {
            String errorMessage = "Рейтинга с Id " + mpaId + " нет.";
            log.warn(errorMessage);
            throw new ObjectDoesntExistException(errorMessage);
        }
    }

    @Override
    public List<Mpa> getAllMpa() {
        log.trace("Получение всех рейтингов");

        List<Mpa> mpas = new ArrayList<>();
        String sql = "SELECT * FROM rating ";
        SqlRowSet srs = jdbcTemplate.queryForRowSet(sql);
        while (srs.next()) {
            mpas.add(new Mpa(srs.getInt("ratingId"), srs.getString("ratingName")));
        }
        return mpas;
    }
}
