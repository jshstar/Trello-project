package com.nbc.trello.column.repository;

import com.nbc.trello.board.Board;
import com.nbc.trello.column.entity.Columns;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ColumnsRepository extends JpaRepository<Columns,Long> {

  List<Columns> findAllByBoardOrderByColumnOrder(Board board);
}
