package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.impl.FilmDbStorage;
import ru.yandex.practicum.filmorate.storage.impl.UserDbStorage;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class FilmorateApplicationTests {
    private final UserDbStorage userStorage;
    private final FilmDbStorage filmStorage;
    private User userTest = new User(
            1L,
            "mo@mu.ru",
            "login",
            "name1",
            LocalDate.of(2002, 02, 04)
    );
    private User userTestUpdated = new User(
            1L,
            "mo@ma.ru",
            "loginUPD",
            "name1UPD",
            LocalDate.of(2004, 04, 04)
    );

    @Test
    public void testCreateUser() {

        Optional<User> userOptional = Optional.ofNullable(userStorage.addUser(userTest));

        assertThat(userOptional)
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("id", 2L)
                );
    }

    @Test
    public void testFindUserById() {

        Optional<User> userOptional = Optional.ofNullable(userStorage.getUser(1L));

        assertThat(userOptional)
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("id", 1L)
                );
    }

    @Test
    public void testUpdateUser() {
        userStorage.addUser(userTest);
        userStorage.updateUser(userTestUpdated);
        Optional<User> userOptional = Optional.ofNullable(userStorage.updateUser(userTestUpdated));

        assertThat(userOptional)
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("email", "mo@ma.ru")
                );
    }

    private Film FilmTest = new Film(
            1L,
            "test1",
            "descr",
            LocalDate.of(2002, 02, 04), 100);


    private Film FilmTestUpdated = new Film(
            2L,
            "f1UPD",
            "descrUPD",
            LocalDate.of(2004, 06, 04), 150);

    @Test
    public void testAddFilm() {
        FilmTest.setMpa(new Mpa(2, "PG"));
        filmStorage.addFilm(FilmTest);
        Optional<Film> userOptional = Optional.ofNullable(filmStorage.addFilm(FilmTest));

        assertThat(userOptional)
                .isPresent()
                .hasValueSatisfying(film ->
                        assertThat(film).hasFieldOrPropertyWithValue("id", 2L)
                );
    }

    @Test
    public void testUpdateFilm() {
        FilmTest.setMpa(new Mpa(2, "PG"));

        filmStorage.addFilm(FilmTest);
        FilmTestUpdated.setMpa(new Mpa(2, "PG"));

        filmStorage.updateFilm(FilmTestUpdated);
        Optional<Film> filmOptional = Optional.ofNullable(filmStorage.updateFilm(FilmTestUpdated));

        assertThat(filmOptional)
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("name", "f1UPD")
                );
    }
}