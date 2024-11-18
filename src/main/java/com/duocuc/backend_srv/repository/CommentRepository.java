package com.duocuc.backend_srv.repository;

import com.duocuc.backend_srv.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}
