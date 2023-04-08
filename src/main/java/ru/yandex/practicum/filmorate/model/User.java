package ru.yandex.practicum.filmorate.model;

import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Pattern;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@RequiredArgsConstructor
@AllArgsConstructor
public class User {
    @Generated
    protected int id;

    @NonNull
    @Pattern(regexp = "\\S+")
    protected String login;

    protected String name;

    @NonNull
    @Email
    protected String email;

    @NonNull
    @PastOrPresent
    protected LocalDate birthday;
}
