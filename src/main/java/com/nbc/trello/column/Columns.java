package com.nbc.trello.column;

import com.nbc.trello.board.domain.Board;
import jakarta.persistence.*;
import lombok.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class Columns {

    @Id
    @GeneratedValue
    private Long id;

    @Setter
    @ManyToOne
    @JoinColumn(name = "board_id")
    private Board board;
}