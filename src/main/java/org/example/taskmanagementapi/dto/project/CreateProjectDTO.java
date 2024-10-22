package org.example.taskmanagementapi.dto.project;

import jakarta.validation.constraints.NotBlank;

public class CreateProjectDTO {

    @NotBlank(message = "name is required")
    private String name;
    private String description;

    public CreateProjectDTO() {
    }

    public CreateProjectDTO(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
