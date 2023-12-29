package com.nbc.trello;

import com.nbc.trello.board.domain.Board;
import com.nbc.trello.column.Columns;
import com.nbc.trello.user.User;
import net.bytebuddy.utility.RandomString;

public class TestDataFactory {

    public static Long userId = 1L;
    public static Long boardId = 1L;
    public static Long columnId = 1L;

    public static User getUser() {
        return User.builder()
                .id(userId++)
                .build();
    }

    public static Board getBoard() {
        String name = RandomString.make(10);
        String description = RandomString.make(10);

        return Board.builder()
                .id(boardId++)
                .name(name)
                .description(description)
                .backgroundColor("#FFFFFF")
                .build();
    }

    public static Columns getColumn() {
        return Columns.builder()
                .id(columnId++)
                .build();
    }
}
