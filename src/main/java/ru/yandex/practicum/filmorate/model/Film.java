package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

@Data

public class Film {
    @JsonIgnore
    private Set<Long> likes = new HashSet<>();
    @JsonIgnore
    private int likesCount;
    private long id;
    @NotBlank
    @NotNull
    private String name;
    @NotBlank
    @Size(max = 200)
    private String description;
    private LocalDate releaseDate;
    @Min(0)
    private Integer duration;
    private Mpa mpa;
    private Set<Genre> genres = new TreeSet<>();

    public Film(long id, String name, String description, LocalDate releaseDate, int duration) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
    }
}