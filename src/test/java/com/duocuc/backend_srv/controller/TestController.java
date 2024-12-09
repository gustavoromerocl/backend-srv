package com.duocuc.backend_srv.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.duocuc.backend_srv.exception.UserAlreadyExistsException;

@RestController
@RequestMapping("/test")
public class TestController {

  @GetMapping("/global-exception")
  public void triggerGlobalException() {
    throw new RuntimeException("Test global exception");
  }

  @GetMapping("/user-exists-exception")
  public void triggerUserAlreadyExistsException() {
    throw new UserAlreadyExistsException("User already exists");
  }
}
