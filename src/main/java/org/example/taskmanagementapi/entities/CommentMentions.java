package org.example.taskmanagementapi.entities;

import jakarta.persistence.*;

@Entity
@Table(
        name = "comment_mentions",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"comment_id", "index"}, name = "UQ_COMMENT_INDEX"),
                @UniqueConstraint(columnNames = {"comment_id", "user_id"}, name = "UQ_COMMENT_MENTIONS"),

        }
)
public class CommentMentions {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "comment_id")
    private Comment comment;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    private long index;

    public CommentMentions() {}

    public CommentMentions(Comment comment, User user, long index) {
        this.comment = comment;
        this.user = user;
        this.index = index;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Comment getComment() {
        return comment;
    }

    public void setComment(Comment comment) {
        this.comment = comment;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public long getIndex() {
        return index;
    }

    public void setIndex(long index) {
        this.index = index;
    }
}
