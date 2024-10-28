package org.example.taskmanagementapi.repositories;

import org.example.taskmanagementapi.entities.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends JpaRepository<Comment,Long> {
    Page<Comment> findAllByTask_Id(Long taskId,Pageable pageable);
}
