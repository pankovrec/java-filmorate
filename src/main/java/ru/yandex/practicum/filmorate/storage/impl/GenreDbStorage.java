package ru.yandex.practicum.filmorate.storage.impl;

import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.GenreStorage;

import java.util.List;

@Component
public class GenreDbStorage implements GenreStorage {

    private final JdbcTemplate jdbcTemplate;

    public GenreDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Genre> getAllGenre() {
        return jdbcTemplate.query("SELECT * FROM GENRES", (rs, rowNum) -> new Genre(
                rs.getInt("genre_id"),
                rs.getString("name")));
    }

    public Genre getGenre(int id) {
        SqlRowSet genreRows = jdbcTemplate.queryForRowSet("SELECT * FROM GENRES WHERE genre_id = ?", id);
        if (genreRows.next()) {
            Genre genre = new Genre(
                    genreRows.getInt("genre_id"),
                    genreRows.getString("name"));

            return genre;
        } else {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    String.format("Жанр с id %d не найден", id));
        }
    }
}