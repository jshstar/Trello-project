package com.nbc.trello.user;

import com.nbc.trello.board.domain.Board;
import com.nbc.trello.board.domain.BoardUser;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
@Getter
@Builder
public class User {

    @Id
    @GeneratedValue
    private Long id;

    private String username;

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
