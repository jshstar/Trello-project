package com.nbc.trello.board.domain;

import com.nbc.trello.global.entity.BaseEntity;
import com.nbc.trello.user.User;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BoardUser extends BaseEntity {

    @EmbeddedId
    private BoardUserId id;

    @Setter
    @MapsId("userId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Setter
    @MapsId("boardId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id")
    private Board board;
}
