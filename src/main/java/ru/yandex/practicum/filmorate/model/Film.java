package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.validation.Valid;
import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@Valid
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
    @PastOrPresent
    private LocalDate releaseDate;
    @Min(0)
    private Integer duration;
}