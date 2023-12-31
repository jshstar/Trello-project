package com.nbc.trello.comment.entity;

import com.nbc.trello.card.entity.Card;
import com.nbc.trello.global.entity.BaseEntity;
import com.nbc.trello.users.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Comment extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String content;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "card_id", nullable = false)
    private Card card;

    public Comment(User user, Card card, String content){
        this.user = user;
        this.card = card;
        this.content = content;
    }

    public void update(String content){
        this.content = content;
    }
}
