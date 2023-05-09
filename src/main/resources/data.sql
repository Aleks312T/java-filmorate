MERGE INTO genre (genreId, genreName)
    VALUES (1, 'Комедия'),
    (2, 'Драма'),
    (3, 'Боевик'),
    (4, 'Ужасы'),
    (5, 'Документальный'),
    (6, 'Триллер'),
    (7, 'Мульфильм');

MERGE INTO rating (ratingId, ratingName)
    VALUES (1, 'G'),
    (2, 'PG'),
    (3, 'PG-13'),
    (4, 'R'),
    (5, 'NC-17');

MERGE INTO friendshipStatus (friendshipStatusId, status)
    VALUES (1, 'Не подтверждённый друг'),
           (2, 'Подтверждённый друг');