package com.nbc.trello.column.entity;

import com.nbc.trello.board.Board;
import com.nbc.trello.card.Card;
import com.nbc.trello.global.entity.BaseEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Columns extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long columnsId;

  @Column(nullable = false)
  private String columnsName;

  @Column(name = "column_order",nullable = false)
  private Integer columnsOrder;

  private Long maxWeight;


  @ManyToOne
  @JoinColumn(name = "board_id", nullable = false)
  private Board board;

  @OneToMany(mappedBy = "column", cascade = CascadeType.REMOVE, orphanRemoval = true)
  private List<Card> cards;

  public void updateColumns(String columnsName) {
    this.columnsName = columnsName;
  }

  public void changeOrders(Integer columnsOrder) { this.columnsOrder = columnsOrder;}
  public Long increaseMaxWeight(){
    return this.maxWeight++;
  }

}