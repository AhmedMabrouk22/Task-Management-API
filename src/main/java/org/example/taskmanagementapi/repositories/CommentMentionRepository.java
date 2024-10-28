package org.example.taskmanagementapi.repositories;

import org.example.taskmanagementapi.entities.CommentMentions;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentMentionRepository extends JpaRepository<CommentMentions,Long> {

}
