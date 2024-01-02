package com.nbc.trello.users.entity;

import com.nbc.trello.board.domain.Board;
import com.nbc.trello.board.domain.BoardUser;
import com.nbc.trello.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "users")
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Setter
    @Column(nullable = false)
    private String password;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    @Builder.Default
    @OneToMany(mappedBy = "user")
    private List<Board> boards = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "user")
    private List<BoardUser> boardUsers = new ArrayList<>();

    public void addBoard(Board board) {
        boards.add(board);
        board.setUser(this);
    }
}
