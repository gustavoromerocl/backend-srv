package com.duocuc.backend_srv.repository;

import com.duocuc.backend_srv.model.Photo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PhotoRepository extends JpaRepository<Photo, Long> {
}
