package com.duocuc.backend_srv.service;

import com.duocuc.backend_srv.model.Comment;
import com.duocuc.backend_srv.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CommentService {

    @Autowired
    private CommentRepository commentRepository;

    public Comment addComment(Comment comment) {
        return commentRepository.save(comment);
    }
}
