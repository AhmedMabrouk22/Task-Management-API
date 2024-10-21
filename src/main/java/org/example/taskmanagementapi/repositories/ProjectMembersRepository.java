package org.example.taskmanagementapi.repositories;

import org.example.taskmanagementapi.entities.ProjectMembers;
import org.example.taskmanagementapi.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProjectMembersRepository extends JpaRepository<ProjectMembers,Long> {

    @Query("select p from ProjectMembers p where p.user.id = :user_id and p.project.id = :project_id")
    Optional<ProjectMembers> findByUser_Id(long user_id, long project_id);

    void deleteById(long id);
}
