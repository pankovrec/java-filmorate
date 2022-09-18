package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.validators.UserValidator;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {

    private Map<Integer, User> users = new HashMap<>();
    private int userId;

    @PostMapping
    public User addUser(@Valid @NotNull @RequestBody User user) throws ValidationException {
        user.setUserId(++userId);
        UserValidator.validate(user);
        users.put(user.getUserId(), user);
        log.info("Добавлен пользователь", user);
        return user;
    }

    @PutMapping
    public User updateUser(@Valid @NotNull @RequestBody User user) throws ValidationException {
        UserValidator.validate(user);
        users.put(user.getUserId(), user);
        log.info("Обновился пользователь", user);
        return user;
    }

    @GetMapping
    public Collection<User> getAll() {
        return users.values();
    }
}