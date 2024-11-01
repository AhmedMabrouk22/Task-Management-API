package org.example.taskmanagementapi.services.task;

import jakarta.transaction.Transactional;
import org.example.taskmanagementapi.dto.task.CreateTaskDTO;
import org.example.taskmanagementapi.dto.task.TaskDTO;
import org.example.taskmanagementapi.dto.task.TaskPageResponseDTO;
import org.example.taskmanagementapi.dto.task.UpdateTaskDTO;
import org.example.taskmanagementapi.entities.Project;
import org.example.taskmanagementapi.entities.ProjectMembers;
import org.example.taskmanagementapi.entities.Task;
import org.example.taskmanagementapi.entities.User;
import org.example.taskmanagementapi.enums.TasksStatus;
import org.example.taskmanagementapi.exceptions.NotFoundExceptionHandler;
import org.example.taskmanagementapi.exceptions.auth.AuthException;
import org.example.taskmanagementapi.mappers.TaskMapper;
import org.example.taskmanagementapi.repositories.TaskRepository;
import org.example.taskmanagementapi.repositories.specifications.TaskSpecification;
import org.example.taskmanagementapi.services.project_members.ProjectMembersService;
import org.example.taskmanagementapi.services.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskServiceImpl implements TaskService{
    private final TaskRepository taskRepository;
    private final ProjectMembersService projectMembersService;
    private final TaskMapper taskMapper;
    private final UserService userService;

    @Autowired
    public TaskServiceImpl(TaskRepository taskRepository,
                           ProjectMembersService projectMembersService,
                           TaskMapper taskMapper, UserService userService) {
        this.taskRepository = taskRepository;
        this.projectMembersService = projectMembersService;
        this.taskMapper = taskMapper;
        this.userService = userService;
    }


    // Check if the fields will update need project manager to update it
    private boolean isNeedAuthToUpdate(UpdateTaskDTO taskDTO) {
        return taskDTO.getTitle() != null || taskDTO.getDescription() != null
                || taskDTO.getDeadline() != null || taskDTO.getAssignToId() != 0 || taskDTO.getPriority() != null;
    }

    private TaskDTO buildTaskDTO(Task task) {
        return taskMapper.toDto(task);
    }

    private void updateFields(Task task, UpdateTaskDTO taskDTO) {
        if (taskDTO.getTitle() != null) {
            task.setTitle(taskDTO.getTitle());
        }

        if (taskDTO.getDescription() != null) {
            task.setDescription(taskDTO.getDescription());
        }

        if (taskDTO.getDeadline() != null) {
            task.setDeadline(taskDTO.getDeadline());
        }

        if (taskDTO.getAssignToId() != 0) {
            User user = userService.findUserById(taskDTO.getAssignToId());
            task.setUser(user);
        }

        if (taskDTO.getPriority() != null) {
            task.setPriority(taskDTO.getPriority());
        }
    }

    @Override
    @Transactional
    public TaskDTO create(CreateTaskDTO taskDTO) {
//        Get logged user and check if is member in this project and has role project manager
        ProjectMembers member = projectMembersService.getProjectMember(taskDTO.getProjectId());
        if (projectMembersService.isNotProjectManager(member)) {
            throw new AuthException("You Unauthorized to add task to this project", HttpStatus.UNAUTHORIZED);
        }

        Project project = member.getProject();

        Task task = taskRepository.save(new Task(
                taskDTO.getTitle(),
                taskDTO.getDescription(),
                TasksStatus.TO_DO,
                taskDTO.getPriority(),
                taskDTO.getDeadline(),
                project
        ));
        return taskMapper.toDto(task);
    }

    @Override
    public Task findTaskById(long taskId) {
        return taskRepository.findById(taskId)
                .orElseThrow(() -> new NotFoundExceptionHandler("Task with id: " + taskId + " not found"));
    }

    @Override
    @Transactional
    public void delete(long taskId) {
        Task task = findTaskById(taskId);
        ProjectMembers member = projectMembersService.getProjectMember(task.getProject().getId());
        if (projectMembersService.isNotProjectManager(member)) {
            throw new AuthException("You Unauthorized to delete task from this project", HttpStatus.UNAUTHORIZED);
        }

        taskRepository.deleteById(taskId);
    }

    @Override
    @Transactional
    public TaskDTO update(long taskId, UpdateTaskDTO taskDTO) {
        Task task = findTaskById(taskId);
        ProjectMembers member = projectMembersService.getProjectMember(task.getProject().getId());

        if (isNeedAuthToUpdate(taskDTO)) {
            // check if the user role is project manager
            if (projectMembersService.isNotProjectManager(member)) {
                throw new AuthException("You Unauthorized to update this task", HttpStatus.UNAUTHORIZED);
            }
            updateFields(task,taskDTO);
        }


        if (taskDTO.getStatus() != null) {
            if (projectMembersService.isNotProjectManager(member) && task.getUser().getId() != member.getId())
                throw new AuthException("You Unauthorized to update this task", HttpStatus.UNAUTHORIZED);
            task.setStatus(taskDTO.getStatus());
        }

        Task updatedTask = taskRepository.save(task);
        TaskDTO updatedTaskDTO =  taskMapper.toDto(updatedTask);
        if (updatedTaskDTO.getAssignTo() != null)
            updatedTaskDTO.getAssignTo().setRole(member.getRole());
        return updatedTaskDTO;
    }

    @Override
    public TaskDTO getTaskById(long taskId) {
        Task task = findTaskById(taskId);
        ProjectMembers member = projectMembersService.getProjectMember(task.getProject().getId());
        if (member == null) {
            throw new AuthException("You Unauthorized to get this task", HttpStatus.UNAUTHORIZED);
        }

        // TODO: add task activity
        // TODO: add task comments

        TaskDTO taskDTO = taskMapper.toDto(task);
        if (taskDTO.getAssignTo() != null)
            taskDTO.getAssignTo().setRole(member.getRole());
        return taskDTO;
    }

    @Override
    public TaskPageResponseDTO getAllTasks(int page, int size,
                                           String[] sort,
                                           String status,
                                           String priority,
                                           Long assignTo) {
        Sort sortBy = Sort.unsorted();
        for (String s : sort)
            sortBy = sortBy.and((s.startsWith("-") ?
                    Sort.by(s.substring(1)).descending() :
                    Sort.by(s).ascending()));

        Pageable pageable = PageRequest.of(page, size, sortBy);

        Specification<Task> spec = Specification
                .where(TaskSpecification.assignToEquals(assignTo))
                .and(TaskSpecification.priorityEquals(priority))
                .and(TaskSpecification.statusEquals(status));

        Page<Task> tasksPage = taskRepository.findAll(spec, pageable);

        List<TaskDTO> taskDTOList = tasksPage.stream()
                .map(this::buildTaskDTO)
                .toList();

        return new TaskPageResponseDTO(
                tasksPage.getTotalPages(),
                tasksPage.getTotalElements(),
                tasksPage.getNumber(),
                taskDTOList
        );
    }

}
