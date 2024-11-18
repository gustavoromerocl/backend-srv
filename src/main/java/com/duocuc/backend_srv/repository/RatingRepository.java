package com.duocuc.backend_srv.repository;

import com.duocuc.backend_srv.model.Rating;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RatingRepository extends JpaRepository<Rating, Long> {
}
