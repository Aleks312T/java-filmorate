package ru.yandex.practicum.filmorate.model;

import jdk.jshell.Snippet;
import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Pattern;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
public class User {
    //TODO: удалить, когда закончу с UserServiseDBImpl
    Set<Integer> friends = new HashSet<>();
    protected Integer id;

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

    public Set<Integer> getFriends() {
        return friends;
    }
}
