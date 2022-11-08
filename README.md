# java-filmorate
Template repository for Filmorate project.
Схема БД.
![](C:\Users\yaya\dev\filmorate\java-filmorate\src\main\resources\filmorate.png)

Пример интаксиса запросов:

`MERGE INTO likes KEY(film_id, user_id) VALUES (?, ?);
`