package org.example.taskmanagementapi.dto.comment;

public class MentionListDTO {
    private long userId;
    private long index;

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public long getIndex() {
        return index;
    }

    public void setIndex(long index) {
        this.index = index;
    }
}
