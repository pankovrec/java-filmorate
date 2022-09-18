package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.controllers.*;
import ru.yandex.practicum.filmorate.model.User;

import java.time.Duration;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class FilmorateApplicationTests {
    @Autowired
    private FilmController filmController;
    @Autowired

    private UserController userController;

    @Test
    public void testAddFilmWithEmptyName() {

        Film film = new Film();
        film.setDescription("Description");
        film.setReleaseDate(LocalDate.of(1990, 02, 01));
        film.setDuration(Duration.ofMinutes(100));

        assertThrows(ValidationException.class, () -> filmController.addFilm(film));
    }

    @Test
    public void testAddFilmWithVeryBigDescription() {
        Film film = new Film();
        film.setName("testover200chardesc");
        film.setDescription("Lorem ipsum dolor sit amet, consectetuer adipiscing elit, sed diam nonummy nibh euismod" +
                " tincidunt ut laoreet dolore magna aliquam erat volutpat. Ut wisi enim ad minim veniam, quis nostrud" +
                " exerci tation ullamcorper suscip");
        film.setReleaseDate(LocalDate.of(2022, 01, 01));
        film.setDuration(Duration.ofMinutes(120));

        assertThrows(ValidationException.class, () -> filmController.addFilm(film));
    }

    @Test
    public void testAddFilmWithIncorrectReleaseDate() {
        Film film = new Film();
        film.setName("testIncorrectReleaseDate");
        film.setDescription("test");
        film.setReleaseDate(LocalDate.of(1700, 01, 01));
        film.setDuration(Duration.ofMinutes(180));


        assertThrows(ValidationException.class, () -> filmController.addFilm(film));
    }

    @Test
    public void testAddFilmWithNegativeDuration() {
        Film film = new Film();
        film.setName("loo");
        film.setDescription("mee");
        film.setReleaseDate(LocalDate.of(2020, 02, 02));
        film.setDuration(Duration.ofMinutes(-100));

        assertThrows(ValidationException.class, () -> filmController.addFilm(film));
    }

    @Test
    public void testAddUserWithIncorrectMail() {
        User user = new User();
        user.setName("user1");
        user.setEmail("test.ur");
        user.setBirthday(LocalDate.of(2020, 02, 01));
        user.setLogin("login1");
        assertThrows(ValidationException.class, () -> userController.addUser(user));
    }

    @Test
    public void testAddUserWithSpaceInLogin() {
        User user = new User();
        user.setName("userwithoutlogin");
        user.setEmail("4@mail.com");
        user.setLogin("");
        user.setBirthday(LocalDate.of(2022, 02, 01));
        assertThrows(ValidationException.class, () -> userController.addUser(user));
    }

    @Test
    public void testAddUserWithSpacesInLogin() {
        User user = new User();
        user.setName("name12");
        user.setEmail("we@a.test");
        user.setBirthday(LocalDate.of(2022, 01, 01));
        user.setLogin("oneword twoword");

        assertThrows(ValidationException.class, () -> userController.addUser(user));
    }

    @Test
    public void testAddUserWithoutName() throws ValidationException {
        User user = new User();
        user.setEmail("test@test.te");
        user.setBirthday(LocalDate.of(2012, 02, 12));
        user.setLogin("loginnn");
        userController.addUser(user);
        assertEquals(user.getName(), user.getLogin());
    }

    @Test
    public void testAddUserWithIncorrectBDay() {
        User user = new User();
        user.setEmail("ter@mail.re");
        user.setBirthday(LocalDate.of(2100, 12, 12));
        user.setLogin("logiinn");
        assertThrows(ValidationException.class, () -> userController.addUser(user));
    }
}