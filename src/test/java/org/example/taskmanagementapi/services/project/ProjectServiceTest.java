package org.example.taskmanagementapi.services.project;

import org.example.taskmanagementapi.dto.project.CreateProjectDTO;
import org.example.taskmanagementapi.dto.project.ProjectResponseDTO;
import org.example.taskmanagementapi.entities.Project;
import org.example.taskmanagementapi.entities.ProjectMembers;
import org.example.taskmanagementapi.entities.User;
import org.example.taskmanagementapi.enums.ProjectRole;
import org.example.taskmanagementapi.exceptions.NotFoundExceptionHandler;
import org.example.taskmanagementapi.exceptions.auth.AuthException;
import org.example.taskmanagementapi.repositories.ProjectMembersRepository;
import org.example.taskmanagementapi.repositories.ProjectRepository;
import org.example.taskmanagementapi.services.auth.AuthService;
import org.example.taskmanagementapi.services.user.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ProjectServiceTest {
    @Mock
    private ProjectRepository projectRepository;
    @Mock
    private UserService userService;
    @Mock
    private ProjectMembersRepository projectMembersRepository;
    @Mock
    private Environment environment;
    @Mock
    private AuthService authService;
    @InjectMocks
    private ProjectServiceImpl projectService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void shouldCreateProjectSuccessfullyAndReturnProjectResponseDTO() {
        User mockUser = new User("Test", "Test@gmail.com","123456789");
        CreateProjectDTO mockProjectDTO = new CreateProjectDTO("Project 1", "Test");
        Project mockProject = new Project();
        mockProject.setName(mockProjectDTO.getName());
        mockProject.setDescription(mockProjectDTO.getDescription());
        ProjectMembers mockMembers = new ProjectMembers();
        mockMembers.setProject(mockProject);
        mockMembers.setUser(mockUser);
        mockMembers.setRole(ProjectRole.PROJECT_MANAGER);
        when(authService.getLoggedUser()).thenReturn(mockUser);
        when(projectRepository.save(mockProject)).thenReturn(new Project("Project 1","Test"));
        when(projectMembersRepository.save(mockMembers)).thenReturn(mockMembers);

        ProjectResponseDTO res = projectService.save(mockProjectDTO);

        assertEquals(res.getName(),mockProject.getName());
        assertNull(res.getProjectMembers());

    }

    @Test
    public void shouldThrowErrorIfUserRoleNotMangerWhenUpdateProject() {
        Project mockProject = new Project("test 1", "project test");
        User mockUser = new User("Test user", "Test@gmail.com","123456789");
        ProjectMembers mockMember = new ProjectMembers(1,mockProject,mockUser,ProjectRole.TEAM_MEMBER);
        long projectId = 1;
        when(projectRepository.findById(projectId)).thenReturn(Optional.of(mockProject));
        when(authService.getLoggedUser()).thenReturn(mockUser);
        when(projectMembersRepository.findByUser_Id(mockUser.getId(),projectId)).thenReturn(Optional.of(mockMember));

        AuthException ex = assertThrows(AuthException.class, ()
                -> projectService.updateById(1,new CreateProjectDTO("project","project")));

        verify(projectRepository,never()).save(any(Project.class));
        assertEquals(HttpStatus.UNAUTHORIZED,ex.getStatusCode());
        assertEquals("You Unauthorized to update this project", ex.getMessage());
    }

    @Test
    public void shouldThrowErrorIfProjectNotFound() {
        long projectId = 1;
        when(projectRepository.findById(projectId)).thenThrow(new NotFoundExceptionHandler("Project with ID " + projectId + " not found"));

        NotFoundExceptionHandler ex = assertThrows(NotFoundExceptionHandler.class,() -> projectService.findProjectById(projectId));
        assertEquals("Project with ID " + projectId + " not found",ex.getMessage());
    }


}
