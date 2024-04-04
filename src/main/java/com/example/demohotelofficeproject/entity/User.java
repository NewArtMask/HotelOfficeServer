package com.example.demohotelofficeproject.entity;

import com.example.demohotelofficeproject.enums.Role;
import com.example.demohotelofficeproject.enums.UserStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.EqualsAndHashCode;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "users")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@EqualsAndHashCode
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID uuid;

    @Column(nullable = false, length = 30)
    private String name;

    @Column(nullable = false, length = 50)
    private String surname;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false, columnDefinition = "varchar(50) default 'ROLE_USER'")
    private Role role;

    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false, columnDefinition = "varchar(50) default 'CREATED'")
    private UserStatus userStatus;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private final List<Hotel> hotels = new ArrayList<>();
}
