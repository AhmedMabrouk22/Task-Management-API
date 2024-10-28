package org.example.taskmanagementapi.services.comment_mention;


import org.example.taskmanagementapi.repositories.CommentMentionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CommentMentionServiceImpl implements CommentMentionService{
    private final CommentMentionRepository commentMentionRepository;

    @Autowired
    public CommentMentionServiceImpl(CommentMentionRepository commentMentionRepository) {
        this.commentMentionRepository = commentMentionRepository;
    }


}
