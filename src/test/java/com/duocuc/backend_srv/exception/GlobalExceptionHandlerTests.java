package com.duocuc.backend_srv.exception;

import static org.hamcrest.Matchers.is;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.duocuc.backend_srv.controller.TestController;
import com.duocuc.backend_srv.service.UserService;
import com.duocuc.backend_srv.util.JwtUtils;

@WebMvcTest(controllers = { TestController.class, GlobalExceptionHandler.class })
@AutoConfigureMockMvc(addFilters = false) // Desactiva los filtros de seguridad
class GlobalExceptionHandlerTest {

  @Autowired
  private MockMvc mockMvc;

  // Simular dependencias externas
  @MockBean
  private JwtUtils jwtUtils;

  @MockBean
  private UserService userService;

  @Test
  void testHandleGlobalException() throws Exception {
    mockMvc.perform(get("/test/global-exception"))
        .andExpect(status().isInternalServerError())
        .andExpect(jsonPath("$.message", is("An unexpected error occurred")))
        .andExpect(jsonPath("$.status", is(500)))
        .andExpect(jsonPath("$.timestamp").exists())
        .andExpect(jsonPath("$.details", is("Test global exception")));
  }

  @Test
  void testHandleUserAlreadyExistsException() throws Exception {
    mockMvc.perform(get("/test/user-exists-exception"))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.message", is("User already exists")))
        .andExpect(jsonPath("$.status", is(404)))
        .andExpect(jsonPath("$.timestamp").exists());
  }
}
