package com.nbc.trello.card.entity;

import com.nbc.trello.column.entity.Columns;
import com.nbc.trello.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "card")
@NoArgsConstructor
public class Card extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 카드 이름
    @Column(name = "titles", length = 30)
    private String titles;

    // 카드 설명
    @Column(name = "description", length = 500)
    private String description;

    // 카드 색상
    @Column(name = "colors", length = 10)
    private String colors;

    @ManyToOne
    @JoinColumn(name = "columns_id")
    private Columns columns;
}