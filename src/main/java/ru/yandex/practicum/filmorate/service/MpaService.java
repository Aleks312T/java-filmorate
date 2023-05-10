package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.storage.dao.film.impl.MpaStorageDBImpl;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MpaService {
    private static final Logger log = LoggerFactory.getLogger(MpaService.class);

    //TODO: в конце поменять обратно
    private final MpaStorageDBImpl mpaStorage;


    public List<Mpa> getAll() {
        return mpaStorage.getAllMpa();
    }

    public Mpa get(Integer id) {
        return mpaStorage.getMpa(id);
    }
}