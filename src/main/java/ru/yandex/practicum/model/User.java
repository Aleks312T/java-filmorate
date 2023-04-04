package ru.yandex.practicum.model;

import lombok.*;

import java.time.LocalDate;
import java.util.Objects;

/*public class User {
    private String email;
    private String login;
    private String name;
    private LocalDate birthday;

    public User(String login, String email, LocalDate birthday)
    {
        this.login = login;
        this.email = email;
        this.birthday = birthday;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public LocalDate getBirthday() {
        return birthday;
    }

    public void setBirthday(LocalDate birthday) {
        this.birthday = birthday;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return email.equals(user.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(email);
    }
}*/

@Data
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
public class User {
    int id;

    @NonNull
    String login;

    String name;

    @NonNull
    String email;

    @NonNull
    LocalDate birthday;
}
