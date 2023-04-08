package ru.yandex.practicum.filmorate.model;

import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.PastOrPresent;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
public class User {
    @Generated
    protected int id;

    @NonNull
    protected String login;

    protected String name;

    @NonNull
    @Email
    protected String email;

    @NonNull
    @PastOrPresent
    protected LocalDate birthday;
}
