package com.duocuc.backend_srv.service;

import com.duocuc.backend_srv.dto.CommentDto;
import com.duocuc.backend_srv.dto.UserDto;
import com.duocuc.backend_srv.model.Comment;
import com.duocuc.backend_srv.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CommentService {

    @Autowired
    private CommentRepository commentRepository;

    public CommentDto addComment(Comment comment) {
        Comment savedComment = commentRepository.save(comment);
        return mapToDTO(savedComment);
    }

    private CommentDto mapToDTO(Comment comment) {
        CommentDto dto = new CommentDto();
        dto.setId(comment.getId());
        dto.setContent(comment.getContent());
        dto.setCreatedAt(comment.getCreatedAt().toString());

        UserDto userDTO = new UserDto();
        userDTO.setId(comment.getUser().getId());
        userDTO.setUsername(comment.getUser().getUsername());
        userDTO.setEmail(comment.getUser().getEmail());

        dto.setUser(userDTO);
        return dto;
    }
}
