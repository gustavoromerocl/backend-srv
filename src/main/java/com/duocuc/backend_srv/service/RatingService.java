package com.duocuc.backend_srv.service;

import com.duocuc.backend_srv.model.Rating;
import com.duocuc.backend_srv.repository.RatingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RatingService {

  @Autowired
  private RatingRepository ratingRepository;

  public Rating addRating(Rating rating) {
    return ratingRepository.save(rating);
  }
}
