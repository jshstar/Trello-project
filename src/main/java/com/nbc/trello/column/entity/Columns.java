package com.nbc.trello.column.entity;

import com.nbc.trello.board.domain.Board;
import com.nbc.trello.card.entity.Card;
import com.nbc.trello.global.entity.BaseEntity;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

import lombok.*;

@Entity
@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Columns extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  @Column(nullable = false)
  private String columnsName;

  private double weight;

  @Setter
  @ManyToOne
  @JoinColumn(name = "board_id", nullable = false)
  private Board board;

  @OneToMany(mappedBy = "columns", cascade = CascadeType.REMOVE, orphanRemoval = true)
  @OrderBy("weight asc")
  private List<Card> cards = new ArrayList<>();

  public void updateColumns(String columnsName) {
    this.columnsName = columnsName;
  }

  public void updateWeight(double weight) {
    this.weight = weight;
  }
}
