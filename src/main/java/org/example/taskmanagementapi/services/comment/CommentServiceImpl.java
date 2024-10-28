package org.example.taskmanagementapi.services.comment;


import jakarta.transaction.Transactional;
import org.example.taskmanagementapi.dto.comment.AddCommentDTO;
import org.example.taskmanagementapi.dto.comment.CommentDTO;
import org.example.taskmanagementapi.dto.comment.CommentPageResponseDTO;
import org.example.taskmanagementapi.dto.comment.MentionListDTO;
import org.example.taskmanagementapi.entities.*;
import org.example.taskmanagementapi.enums.ProjectRole;
import org.example.taskmanagementapi.exceptions.DatabaseException;
import org.example.taskmanagementapi.exceptions.NotFoundExceptionHandler;
import org.example.taskmanagementapi.exceptions.auth.AuthException;
import org.example.taskmanagementapi.mappers.CommentMapper;
import org.example.taskmanagementapi.repositories.CommentRepository;
import org.example.taskmanagementapi.services.comment_mention.CommentMentionService;
import org.example.taskmanagementapi.services.project_members.ProjectMembersService;
import org.example.taskmanagementapi.services.task.TaskService;
import org.example.taskmanagementapi.services.user.UserService;
import org.example.taskmanagementapi.utils.PageUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class CommentServiceImpl implements CommentService{
    private final CommentRepository commentRepository;
    private final ProjectMembersService projectMembersService;
    private final TaskService taskService;
    private final CommentMapper commentMapper;
    private final CommentMentionService commentMentionService;
    private final UserService userService;
    @Autowired
    public CommentServiceImpl(CommentRepository commentRepository,
                              ProjectMembersService projectMembersService,
                              TaskService taskService,
                              CommentMapper commentMapper,
                              CommentMentionService commentMentionService,
                              UserService userService) {
        this.commentRepository = commentRepository;
        this.projectMembersService = projectMembersService;
        this.taskService = taskService;
        this.commentMapper = commentMapper;
        this.commentMentionService = commentMentionService;
        this.userService = userService;
    }

    private List<CommentMentions> BuildMentionsList(List<MentionListDTO> mentionListDTO, Comment comment) {
        if (mentionListDTO == null) return new ArrayList<>();
        List<CommentMentions> mentions = new ArrayList<>();
        mentionListDTO.forEach(mention -> {
            User user = userService.findUserById(mention.getUserId());
            mentions.add(new CommentMentions(comment,user,mention.getIndex()));
        });
        return mentions;
    }

    private Comment saveComment(Comment comment) {
        try {
            return commentRepository.save(comment);
        } catch (DataIntegrityViolationException ex) {
            throw new DatabaseException("user and index must be unique for each comment");
        }
    }

    @Override
    public Comment findCommentById(Long commentId) {
        return commentRepository.findById(commentId)
                .orElseThrow(() -> new NotFoundExceptionHandler("Comment with id: " + commentId + " not exist"));
    }

    @Override
    @Transactional
    public CommentDTO add(AddCommentDTO commentDTO) {
        // Get task
        Task task = taskService.findTaskById(commentDTO.getTaskId());
        // Check if user member in the project
        ProjectMembers member = projectMembersService.getProjectMember(task.getProject().getId());

        Comment comment = new Comment();
        comment.setTask(task);
        comment.setContent(commentDTO.getContent());
        comment.setUser(member.getUser());
        comment.setMentions(BuildMentionsList(commentDTO.getMentions(),comment));
        return commentMapper.toDTO(saveComment(comment));
    }

    @Override
    @Transactional
    public CommentDTO update(Long commentId, AddCommentDTO commentDTO) {
        Comment comment = findCommentById(commentId);
        ProjectMembers member = projectMembersService.getProjectMember(comment.getTask().getProject().getId());
        if (member.getUser().getId() != comment.getUser().getId()) {
            throw new AuthException("You unauthorized to update this comment", HttpStatus.UNAUTHORIZED);
        }

        comment.setContent(commentDTO.getContent());
        comment.getMentions().forEach(mention -> {
            commentRepository.findById(mention.getId());
        });
        comment.setMentions(BuildMentionsList(commentDTO.getMentions(),comment));
        return commentMapper.toDTO(saveComment(comment));
    }

    @Override
    @Transactional
    public void delete(Long commentId) {
        Comment comment = findCommentById(commentId);
        ProjectMembers member = projectMembersService.getProjectMember(comment.getTask().getProject().getId());
        if (member.getUser().getId() != comment.getUser().getId() && member.getRole() != ProjectRole.PROJECT_MANAGER) {
            throw new AuthException("You unauthorized to update this comment", HttpStatus.UNAUTHORIZED);
        }
        commentRepository.deleteById(commentId);
    }

    @Override
    public CommentPageResponseDTO getComments(Long taskId, int page, int size) {
        Task task = taskService.findTaskById(taskId);
        projectMembersService.getProjectMember(task.getProject().getId());
        Pageable pageable = PageRequest.of(page,size);
        Page<Comment> commentsPage = commentRepository.findAllByTask_Id(taskId,pageable);
        List<CommentDTO> comments = PageUtils.convertPage(commentsPage,commentMapper::toDTO);

        return new CommentPageResponseDTO(commentsPage.getTotalPages(),
                commentsPage.getTotalElements(),
                commentsPage.getNumber(),
                comments);
    }


}
