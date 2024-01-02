package com.nbc.trello.worker.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.nbc.trello.worker.entity.Worker;
import com.nbc.trello.worker.entity.WorkerID;

@Repository
public interface WorkerRepository extends JpaRepository<Worker, WorkerID> {

	@Query("SELECT CASE WHEN COUNT(w) > 0 THEN true ELSE false END FROM Worker w "
		+ "WHERE w.id.cardId = :cardId AND w.id.userId = :userId")
	boolean existsByCardIdAndUserId(Long cardId, Long userId);
}
