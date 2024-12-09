package com.duocuc.backend_srv.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserAlreadyExistsExceptionTest {

  @Test
  void testExceptionMessage() {
    // Dado
    String expectedMessage = "User already exists";

    // Cuando
    UserAlreadyExistsException exception = new UserAlreadyExistsException(expectedMessage);

    // Entonces
    assertNotNull(exception);
    assertEquals(expectedMessage, exception.getMessage());
  }

  @Test
  void testExceptionThrown() {
    // Dado
    String expectedMessage = "User already exists";

    // Entonces
    Exception exception = assertThrows(UserAlreadyExistsException.class, () -> {
      // Cuando
      throw new UserAlreadyExistsException(expectedMessage);
    });

    assertEquals(expectedMessage, exception.getMessage());
  }
}
