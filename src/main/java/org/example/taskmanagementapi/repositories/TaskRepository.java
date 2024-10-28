package org.example.taskmanagementapi.repositories;

import org.example.taskmanagementapi.entities.Task;
import org.example.taskmanagementapi.enums.TaskPriority;
import org.example.taskmanagementapi.enums.TasksStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface TaskRepository extends JpaRepository<Task,Long>, JpaSpecificationExecutor<Task> {

    @Override
    Page<Task> findAll(Specification<Task> specification,Pageable pageable);
    Page<Task> findByPriority(TaskPriority priority, Pageable pageable);
    Page<Task> findByStatus(TasksStatus status, Pageable pageable);
    Page<Task> findByUser_Id(Long user_id, Pageable pageable);
}
