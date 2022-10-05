package ru.yandex.practicum.filmorate.model;

import lombok.*;

import javax.validation.Valid;
import javax.validation.constraints.*;
import java.time.LocalDate;

@Data
@Valid
public class Film {
    @NotNull
    @Positive
    private long id;
    @NotBlank
    @NotNull
    private String name;
    @NotBlank
    @Size(max = 200)
    private String description;
    @PastOrPresent
    private LocalDate releaseDate;
    @Min(0)
    private Integer duration;
}