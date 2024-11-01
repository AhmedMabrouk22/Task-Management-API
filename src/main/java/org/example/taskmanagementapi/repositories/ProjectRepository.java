package org.example.taskmanagementapi.repositories;

import org.example.taskmanagementapi.entities.Project;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProjectRepository extends JpaRepository<Project,Long> {
    Optional<Project> findById(long id);

    Page<Project> findAllByTeamMembers_UserId(long user_id, Pageable pageable);
}
