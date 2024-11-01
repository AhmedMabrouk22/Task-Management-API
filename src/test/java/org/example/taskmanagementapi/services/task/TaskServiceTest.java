package org.example.taskmanagementapi.services.task;

import org.example.taskmanagementapi.dto.task.CreateTaskDTO;
import org.example.taskmanagementapi.dto.task.TaskDTO;
import org.example.taskmanagementapi.entities.Project;
import org.example.taskmanagementapi.entities.ProjectMembers;
import org.example.taskmanagementapi.entities.Task;
import org.example.taskmanagementapi.entities.User;
import org.example.taskmanagementapi.enums.ProjectRole;
import org.example.taskmanagementapi.enums.TaskPriority;
import org.example.taskmanagementapi.enums.TasksStatus;
import org.example.taskmanagementapi.mappers.TaskMapper;
import org.example.taskmanagementapi.repositories.TaskRepository;
import org.example.taskmanagementapi.services.project_members.ProjectMembersService;
import org.example.taskmanagementapi.services.user.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;
    @Mock
    private ProjectMembersService projectMembersService;
    @Mock
    private TaskMapper taskMapper;
    @Mock
    private UserService userService;
    @InjectMocks
    private TaskServiceImpl taskService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void shouldCreateTaskSuccessfully() {

        Project mockProject = new Project("test project","test project");
        User mockUser = new User("test user", "test user", "test");
        ProjectMembers mockMember = new ProjectMembers(1,mockProject,mockUser, ProjectRole.PROJECT_MANAGER);
        CreateTaskDTO taskDTO = new CreateTaskDTO("test task","test task",0,null, TaskPriority.LOW);
        Task mockTask = new Task("test task","test task", TasksStatus.TO_DO,taskDTO.getPriority(),taskDTO.getDeadline(),mockProject);
        TaskDTO taskRes = new TaskDTO(0,mockTask.getTitle(),mockTask.getDescription(),mockTask.getDeadline(),mockTask.getStatus(),mockTask.getPriority(), LocalDateTime.now());

        when(projectMembersService.getProjectMember(mockProject.getId())).thenReturn(mockMember);
        when(taskRepository.save(any(Task.class))).thenReturn(mockTask);
        when(taskMapper.toDto(mockTask)).thenReturn(taskRes);

        TaskDTO res = taskService.create(taskDTO);

        assertEquals(TasksStatus.TO_DO,res.getStatus());

    }

}
