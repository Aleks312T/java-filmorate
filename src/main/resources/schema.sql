--TODO: убрать перед сдачей проекта (наверное)
--Дропаем именно в этом порядке из-за зависимостей
--DROP TABLE IF EXISTS friendshipStatus;
--DROP TABLE IF EXISTS friends;
--DROP TABLE IF EXISTS likes;
--DROP TABLE IF EXISTS films;
--DROP TABLE IF EXISTS users;
--DROP TABLE IF EXISTS rating;
--DROP TABLE IF EXISTS genre;

CREATE TABLE IF NOT EXISTS genre (
  genreId INT NOT NULL GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
  genreName VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS rating (
  ratingId INT NOT NULL GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
  ratingName VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS films (
  filmId INT NOT NULL GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
  filmName VARCHAR(255) NOT NULL,
  description VARCHAR(255) NOT NULL,
  releaseDate TIMESTAMP,
  genreId INT NOT NULL REFERENCES genre(genreId) ON DELETE CASCADE,
  duration INT NOT NULL,
  ratingId VARCHAR(255) REFERENCES rating(ratingId) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS users (
  userId INT NOT NULL GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
  login VARCHAR(255) NOT NULL,
  userName VARCHAR(255) NOT NULL,
  email VARCHAR(255) NOT NULL,
  birthday TIMESTAMP
);

CREATE TABLE IF NOT EXISTS friends (
  userId INT NOT NULL REFERENCES users(userId),
  friendId INT NOT NULL REFERENCES users(userId),
  friendshipStatusId INT NOT NULL
);

CREATE TABLE IF NOT EXISTS friendshipStatus (
  friendshipStatusId INT NOT NULL GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
  status VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS likes (
  filmId INT NOT NULL REFERENCES films(filmId),
  userId INT NOT NULL REFERENCES users (userId)
);

COMMENT ON COLUMN genre.genreName IS 'Жанр фильма';

COMMENT ON COLUMN rating.ratingName IS 'Возрастной рейтинг';

COMMENT ON COLUMN films.filmName IS 'Название фильма';

COMMENT ON COLUMN films.description IS 'Описание фильма';

COMMENT ON COLUMN films.releaseDate IS 'Дата выхода фильма';

COMMENT ON COLUMN films.duration IS 'Длительность фильма в минутах';

COMMENT ON COLUMN users.login IS 'Логин пользователя';

COMMENT ON COLUMN users.userName IS 'Имя пользователя';

COMMENT ON COLUMN users.email IS 'Электронная почта пользователя';

COMMENT ON COLUMN users.birthday IS 'День рождения пользователя';

COMMENT ON COLUMN friendshipStatus.status IS 'Статус дружбы между пользователями';