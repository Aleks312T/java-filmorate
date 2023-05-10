package ru.yandex.practicum.filmorate.dao.impl;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.interf.FilmStorageDB;
import ru.yandex.practicum.filmorate.exception.ObjectDoesntExistException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;

@RequiredArgsConstructor
@Component
public class FilmStorageDBImpl implements FilmStorageDB {

    private static final Logger log = LoggerFactory.getLogger(FilmStorageDBImpl.class);

    private final JdbcTemplate jdbcTemplate;

    public Collection<Film> findAll() {
        log.trace("Получение всех фильмов");
        String sql = "SELECT * FROM films AS f " +
                     "JOIN rating AS r ON f.ratingId = r.ratingId " +
                     "LEFT JOIN filmGenres AS fg ON fg.filmId = f.filmId " +
                     "LEFT JOIN genre AS g ON g.genreId = fg.genreId";
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
        //disconnectFilmGenre(film);

        if (film.getGenres() != null) {
            disconnectFilmGenre(film.getId());
            connectFilmGenre(film);
        } else {
            film.setGenres(new HashSet<>());
        }

        return film;
    }

    public Film updateFilm(Film film) {
        log.trace("Изменение фильма");
        String sql = "UPDATE films " +
                     "SET filmName = ?, description = ?, releaseDate = ?, duration = ?, ratingId = ? " +
                     "WHERE filmId = ?";

        jdbcTemplate.update(sql,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getMpa().getId(),
                film.getId());

        //disconnectFilmGenre(film);

        if (film.getGenres() != null) {
            disconnectFilmGenre(film.getId());
            connectFilmGenre(film);
        } else {
            film.setGenres(new HashSet<>());
        }

        return getFilm(film.getId());
    }

    public Film getFilm(Integer filmId) {
        log.trace("Получение фильма");
        String sql = "SELECT * FROM films AS f " +
                "JOIN rating AS r ON f.ratingId = r.ratingId " +
                "LEFT JOIN filmGenres AS fg ON fg.filmId = f.filmId " +
                "LEFT JOIN genre AS g ON g.genreId = fg.genreId " +
                "LEFT JOIN likes AS l ON l.filmId = f.filmId " +
                "WHERE f.filmId = ?";
        //String sql = "SELECT * FROM films WHERE filmId=?";

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
        String sql = "INSERT INTO likes (filmId, userId) "
                + "VALUES (?, ?)";
        jdbcTemplate.update(sql, filmId, userId);

//        Film result = getFilm(filmId);
//        Set<Integer> set = result.getLikes();
//        set.add(userId);
//        result.setLikes(set);
//        return result;

        return getFilm(filmId);
    }

    public Film removeLike(Integer filmId, Integer userId) {
        String sql = "DELETE FROM likes " +
                    "WHERE (USERID IN (?, ?) AND filmId IN (?, ?)) ";
        jdbcTemplate.update(sql, filmId, userId, filmId, userId);

//        Film result = getFilm(filmId);
//        Set<Integer> set = result.getLikes();
//        set.remove(userId);
//        result.setLikes(set);
//        return result;

        return getFilm(filmId);
    }

    public boolean getLike(Integer filmId, Integer userId) {
        String sql = "DELETE FROM likes "
                + "WHERE filmId = ? AND userId = ?";
        jdbcTemplate.update(sql, filmId, userId);
        return true;
    }

    public Collection<Film> getPopularFilm(Integer count) {
        String sql = "SELECT * " +
                "FROM films f " +
                "JOIN rating AS r ON f.ratingId = r.ratingId " +
                "LEFT JOIN filmGenres AS fg ON fg.filmId = f.filmId " +
                "LEFT JOIN genre AS g ON g.genreId = fg.genreId " +
                "LEFT JOIN likes AS l ON l.filmId = f.filmId " +
                "WHERE f.filmId IN (SELECT f1.filmId " +
                                "FROM FILMS f1 " +
                                "LEFT JOIN likes l ON f1.filmID = l.filmID " +
                                "GROUP BY f1.filmId " +
                                "ORDER BY COUNT(l.userId) DESC " +
                                "LIMIT ?)";
        Collection<Film> result = jdbcTemplate.query(sql, this::filmRowMapper, count);

        //TreeMap<Integer, Integer> result = new TreeMap<>();

//        List<Film> result = new ArrayList<>();
//        String sql = "SELECT filmId " +
//                     "FROM films " +
//                     "GROUP BY filmId " +
//                     "ORDER BY COUNT(userId) DESC " +
//                     "LIMIT ?";
//        SqlRowSet popularFilms = jdbcTemplate.queryForRowSet(sql, count);
//        while(popularFilms.next()) {
//            result.add(getFilm(Integer.parseInt(popularFilms.getString("filmId"))));
//        }

        return result;
//        return findAll().stream()
//                .sorted((o1, o2) -> Integer.compare(o2.getLikes().size(), o1.getLikes().size()))
//                .limit(count)
//                .collect(Collectors.toSet());
    }

    private int getNewFilmId() {
        String sql = "SELECT filmId \n" +
                          "FROM films f \n" +
                          "ORDER BY filmId DESC \n" +
                          "LIMIT 1";

        List<Integer> id = jdbcTemplate.query(sql, (rs, rowNum) -> rs.getInt("filmId"));
        return id.get(0);
    }

    private void disconnectFilmGenre(int filmId) {
        log.trace("Отрываем жанры от фильма с Id {}", filmId);
        String sql = "DELETE FROM filmGenres WHERE filmId = ?";
        jdbcTemplate.update(sql, filmId);
    }

    private void connectFilmGenre(Film film) {
        log.trace("Подключаем жанры к фильму с Id {}", film.getId());
        String sql = "INSERT INTO filmGenres (filmId, genreId) VALUES (?,?)";
        jdbcTemplate.batchUpdate(sql,
                new BatchPreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement ps, int i) throws SQLException {
                        Genre genre = new ArrayList<>(film.getGenres()).get(i);
                        ps.setInt(1,film.getId());
                        ps.setInt(2,genre.getId());
                    }

                    @Override
                    public int getBatchSize() {
                        return film.getGenres().size();
                    }
                });

    }

    private Set<Genre> getGenres(int filmId) {
        Comparator<Genre> compId = Comparator.comparing(Genre::getId);
        Set<Genre> genres = new TreeSet<>(compId);
        String sql = "SELECT filmGenres.genreId, genre.genreName FROM filmGenres "
                + "JOIN genre ON genre.genreId = filmGenres.genreId "
                + "WHERE filmId = ? ORDER BY genreId ASC";
        genres.addAll(jdbcTemplate.query(sql, this::genreMap, filmId));
        return genres;
    }

    private Genre genreMap(ResultSet rs, int id) throws SQLException {
        int genreId = rs.getInt("genreId");
        String genreName = rs.getString("genreName");
        return Genre.builder()
                .id(genreId)
                .name(genreName)
                .build();
    }

    private Film filmMap(SqlRowSet srs) {
        int filmId = srs.getInt("filmId");
        String filmName = srs.getString("filmName");
        String description = srs.getString("description");
        LocalDate releaseDate = Objects.requireNonNull(srs.getTimestamp("releaseDate"))
                .toLocalDateTime().toLocalDate();
        int duration = srs.getInt("duration");
        int mpaId = srs.getInt("ratingId");

        String mpaName = null;
        if(srs.findColumn("ratingName") != 0)
            mpaName = srs.getString("ratingName");

        Mpa mpa = Mpa.builder()
                .id(mpaId)
                .name(mpaName)
                .build();

        Set<Genre> genres = getGenres(filmId);

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

    private Film filmRowMapper(ResultSet rs, int rowNum) throws SQLException {
        Film film = Film.builder()
                .id(rs.getInt("filmId"))
                .name(rs.getString("filmName"))
                .description(rs.getString("description"))
                .releaseDate(rs.getDate("releaseDate").toLocalDate())
                .duration(rs.getInt("duration"))
                .mpa(new Mpa(rs.getInt("ratingId"), rs.getString("ratingName")))
                .build();

        if (film.getGenres() != null) {
            disconnectFilmGenre(film.getId());
            connectFilmGenre(film);
        } else {
            film.setGenres(new HashSet<>());
        }
        return film;
    }
}
