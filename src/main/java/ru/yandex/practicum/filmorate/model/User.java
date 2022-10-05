package ru.yandex.practicum.filmorate.model;

import lombok.*;

import javax.validation.constraints.*;
import java.time.LocalDate;

@Data
public class User {
    @NotNull
    @Positive
    private long id;
    @NotBlank
    @Email
    private String email;
    @NotBlank
    @Pattern(regexp = "\\S+")
    private String login;
    private String name;
    @PastOrPresent
    private LocalDate birthday;
}