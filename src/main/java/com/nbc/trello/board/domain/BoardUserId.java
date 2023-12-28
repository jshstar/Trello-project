package com.nbc.trello.board.domain;


import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class BoardUserId implements Serializable {

    private Long userId;
    private Long boardId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BoardUserId that = (BoardUserId) o;
        return Objects.equals(getUserId(), that.getUserId()) && Objects.equals(getBoardId(), that.getBoardId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getUserId(), getBoardId());
    }
}
