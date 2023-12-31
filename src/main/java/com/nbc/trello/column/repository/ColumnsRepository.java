package com.nbc.trello.column.repository;

import com.nbc.trello.board.domain.Board;
import com.nbc.trello.column.entity.Columns;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ColumnsRepository extends JpaRepository<Columns,Long> {

  List<Columns> findAllByBoardOrderByWeightAsc(Board board);

  Optional<Columns> findFirstByBoardOrderByWeightDesc(Board board);

  @Query("select c from Columns c left join fetch c.cards where c.id in :ids order by c.weight")
  List<Columns> findAllByIdIn(List<Long> ids);
}
