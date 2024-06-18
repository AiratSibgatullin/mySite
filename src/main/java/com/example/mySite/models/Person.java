package com.example.mySite.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "Person")
@ToString
@Getter
@Setter
public class Person {
    public Person() {
    }

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotBlank(message = "Имя не может состоять из пробелов")
    @Size(min = 2, max = 25, message = "Имя должно содержать от 2 до 25 символов")
    @Column(name = "username")
    private String username;

    @Email(message = "не соответствует формату email: user@mail.ru")
    @Column(name = "email")
    private String email;

    @Size(min = 6, message = "Пароль должен содержать от 6 до 12 символов")
    @Column(name = "password")
    private String password;

    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    private Role role;
}
