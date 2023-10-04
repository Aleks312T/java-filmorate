## Проект "FilmoRate".
*Сервис, который будет работать с фильмами и оценками пользователей.*

Приложение Filmorate сделано Spring Boot.

---
Ниже представлена ER модель хранения данных в данном проекте.
### **База данных:** ###
![Схема таблиц базы данных](./resources/ER of DB.png)

**Пример запросов для фильмов:**

*1. Получить все фильмы*
```SQL
SELECT *
FROM films;
```
*2. Получить фильм по id*
```SQL
SELECT *
FROM films
WHERE filmId = ?;
```
*3. Получить 10 самых популярных фильмов*
```SQL
SELECT * 
FROM films f 
JOIN rating ON f.ratingId = rating.ratingId
WHERE f.filmId IN (SELECT f.filmId FROM films f LEFT JOIN likes fl ON f.filmId = fl.filmId
GROUP BY f.filmId ORDER BY COUNT(fl.userId) DESC LIMIT ?)
```


**Пример запросов для пользователей:**

*1. Получить всех пользователей*
```SQL
SELECT * 
FROM users
```
*2. Получить пользователя по id*
```SQL
SELECT * 
FROM users 
WHERE userId = ?
```
*3. Получить всех друзей пользователя*
```SQL
SELECT u.* 
FROM friends f 
JOIN users u ON f.friendId = u.userId 
WHERE f.userId = ?
```
*4. Получить общих друзей*
```SQL
SELECT * 
FROM users
WHERE userId IN (SELECT f1.friendId FROM friends f1 
LEFT JOIN friends f2 ON f1.friendId = f2.friendId
WHERE F1.userId = ? AND F2.userId = ?)
```
