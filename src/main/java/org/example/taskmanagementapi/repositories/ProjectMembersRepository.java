package org.example.taskmanagementapi.repositories;

import org.example.taskmanagementapi.annotations.TrackExecutionTime;
import org.example.taskmanagementapi.dto.project.ProjectMemberDTO;
import org.example.taskmanagementapi.entities.ProjectMembers;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProjectMembersRepository extends JpaRepository<ProjectMembers,Long> {

    @Query("select p from ProjectMembers p where p.user.id = :user_id and p.project.id = :project_id")
    Optional<ProjectMembers> findByUser_Id(long user_id, long project_id);

    @Query("delete from ProjectMembers where user.id = :user_id and project.id = :project_id")
    @Modifying
    void deleteByUser_IdAndProject_Id(long user_id,long project_id);

    @Query(
            "select new org.example.taskmanagementapi.dto.project.ProjectMemberDTO(u.id,u.name,u.email,u.image,p.role)" +
                    " from ProjectMembers p left join User u on p.user.id = u.id where p.project.id = :projectId"
    )
//    @TrackExecutionTime
    Page<ProjectMemberDTO> findByProject_Id(long projectId, Pageable pageable);
}
