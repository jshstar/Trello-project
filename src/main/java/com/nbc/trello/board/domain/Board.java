package com.nbc.trello.board.domain;

import com.nbc.trello.board.dto.request.BoardCreateRequest;
import com.nbc.trello.board.dto.request.BoardUpdateRequest;
import com.nbc.trello.column.entity.Columns;
import com.nbc.trello.global.entity.BaseEntity;
import com.nbc.trello.users.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class Board extends BaseEntity {

    @Id
    @GeneratedValue
    private Long id;

    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Builder.Default
    @OrderBy("weight ASC")
    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL)
    private List<Columns> columns = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL)
    private List<BoardUser> boardUsers = new ArrayList<>();

    @Column(length = 50)
    private String name;

    @Column(length = 10)
    private String backgroundColor;

    @Column(length = 255)
    private String description;

    public Board(BoardCreateRequest request) {
        name = request.getName();
    }

    public void update(BoardUpdateRequest request) {
        name = request.getName();
        backgroundColor = request.getBackgroundColor();
        description = request.getDescription();
    }

    public void addColumn(Columns column) {
        columns.add(column);
        column.setBoard(this);
    }

    public void addBoardUser(BoardUser boardUser) {
        boardUsers.add(boardUser);
        boardUser.setBoard(this);
    }
}
