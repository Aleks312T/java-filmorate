package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.storage.dao.film.impl.GenreStorageDBImpl;
import ru.yandex.practicum.filmorate.exception.ObjectDoesntExistException;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GenreDBService {
    private static final Logger log = LoggerFactory.getLogger(UserDBService.class);

    private final GenreStorageDBImpl genreStorage;

    public List<Genre> getAll() {
        return genreStorage.getAllGenres();
    }

    public Genre get(Integer genreId) {
        if (genreId == null) {
            String errorMessage = "Отсутствует входной идентификатор";
            log.warn(errorMessage);
            throw new NullPointerException(errorMessage);
        }
        Genre result = genreStorage.getGenre(genreId);
        if (result == null) {
            String errorMessage = "Пользователя с Id " + genreId + " нет.";
            log.warn(errorMessage);
            throw new ObjectDoesntExistException(errorMessage);
        } else
            return genreStorage.getGenre(genreId);
    }
}
