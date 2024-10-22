package org.example.taskmanagementapi.dto.project;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.example.taskmanagementapi.dto.user.UserDTO;
import org.example.taskmanagementapi.enums.ProjectRole;

@JsonIgnoreProperties({"lastPasswordChange"})
public class ProjectMemberDTO extends UserDTO {
    private ProjectRole role;

    public ProjectMemberDTO() {
    }

    public ProjectMemberDTO(long id, String name, String email, String image, ProjectRole role) {
        super(id,name,email,image);
        this.role = role;
    }
    public ProjectRole getRole() {
        return role;
    }

    public void setRole(ProjectRole role) {
        this.role = role;
    }
}
