package com.nbc.trello.users;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
@Table(name = "user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column
    private String pastPassword;


    public User(String username, String password){
        this.username = username;
        this.password = password;

    }


    public void setPastPassword(String password) {
        this.password=password;
    }

    public void setPassword(String afterPassword) {
        this.password=afterPassword;
    }
}