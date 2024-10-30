package org.example.taskmanagementapi.services.project;

import org.example.taskmanagementapi.entities.User;
import org.example.taskmanagementapi.exceptions.auth.AuthException;
import org.example.taskmanagementapi.repositories.ProjectMembersRepository;
import org.example.taskmanagementapi.services.auth.AuthService;
import org.example.taskmanagementapi.services.project_members.ProjectMemberServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

class ProjectMemberServiceTest {

    @Mock
    private ProjectMembersRepository projectMembersRepository;
    @Mock
    private AuthService authService;
    @InjectMocks
    private ProjectMemberServiceImpl projectMemberService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void shouldThrowErrorIfProjectMemberNotFound() {
        long projectId = 1;
        User mockUser = new User("test user", "test@gmail.com", "12345679");

        when(authService.getLoggedUser()).thenReturn(mockUser);
        when(projectMembersRepository.findByUser_Id(mockUser.getId(),projectId)).thenThrow(new AuthException("You unauthorized to access this project", HttpStatus.UNAUTHORIZED));

        AuthException ex = assertThrows(AuthException.class,() -> projectMemberService.getProjectMember(projectId));

        assertEquals(HttpStatus.UNAUTHORIZED, ex.getStatusCode());
        assertEquals("You unauthorized to access this project", ex.getMessage());
    }
}
