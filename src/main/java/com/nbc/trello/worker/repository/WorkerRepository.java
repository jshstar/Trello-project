package com.nbc.trello.worker.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.nbc.trello.worker.entity.Worker;
import com.nbc.trello.worker.entity.WorkerID;

@Repository
public interface WorkerRepository extends JpaRepository<Worker, WorkerID> {

}
