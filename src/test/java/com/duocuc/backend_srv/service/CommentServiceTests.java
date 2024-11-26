package com.duocuc.backend_srv.service;

import com.duocuc.backend_srv.dto.CommentDto;
import com.duocuc.backend_srv.model.Comment;
import com.duocuc.backend_srv.model.User;
import com.duocuc.backend_srv.repository.CommentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CommentServiceTest {

  @InjectMocks
  private CommentService commentService;

  @Mock
  private CommentRepository commentRepository;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void testAddCommentSuccess() {
    // Mock user
    User mockUser = new User();
    mockUser.setId(1L);
    mockUser.setUsername("testuser");
    mockUser.setEmail("testuser@example.com");

    // Mock comment
    Comment mockComment = new Comment();
    mockComment.setId(1L);
    mockComment.setContent("This is a test comment.");
    mockComment.setCreatedAt(LocalDateTime.of(2024, 1, 1, 12, 0));
    mockComment.setUser(mockUser);

    // Mock repository behavior
    when(commentRepository.save(any(Comment.class))).thenReturn(mockComment);

    // Test
    CommentDto result = commentService.addComment(mockComment);

    // Verify interactions
    verify(commentRepository, times(1)).save(mockComment);

    // Assert results
    assertNotNull(result);
    assertEquals(1L, result.getId());
    assertEquals("This is a test comment.", result.getContent());
    assertEquals("2024-01-01T12:00", result.getCreatedAt());
    assertNotNull(result.getUser());
    assertEquals(1L, result.getUser().getId());
    assertEquals("testuser", result.getUser().getUsername());
    assertEquals("testuser@example.com", result.getUser().getEmail());
  }
}
