package org.example.taskmanagementapi.dto.comment;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public class AddCommentDTO {

    @NotNull(message = "task id is required")
    @JsonProperty(value = "task_id")
    private Long taskId;

    @NotBlank(message = "comment must has content")
    private String content;
    private List<MentionListDTO> mentions;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<MentionListDTO> getMentions() {
        return mentions;
    }

    public void setMentions(List<MentionListDTO> mentions) {
        this.mentions = mentions;
    }

    public Long getTaskId() {
        return taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }

}
