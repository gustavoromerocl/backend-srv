package com.duocuc.backend_srv.service;

import static org.mockito.Mockito.*;

import com.duocuc.backend_srv.model.Role;
import com.duocuc.backend_srv.repository.RoleRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

class RoleInitializerTest {

  @InjectMocks
  private RoleInitializer roleInitializer;

  @Mock
  private RoleRepository roleRepository;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void testInitRolesWhenRolesDoNotExist() {
    // Dado: Los roles no existen
    when(roleRepository.findByCode("ROLE_ADMIN")).thenReturn(Optional.empty());
    when(roleRepository.findByCode("ROLE_USER")).thenReturn(Optional.empty());

    // Cuando
    roleInitializer.initRoles();

    // Entonces: Los roles se crean
    verify(roleRepository, times(1)).findByCode("ROLE_ADMIN");
    verify(roleRepository, times(1)).findByCode("ROLE_USER");

    verify(roleRepository, times(1))
        .save(argThat(role -> "ADMIN".equals(role.getName()) && "ROLE_ADMIN".equals(role.getCode())));
    verify(roleRepository, times(1))
        .save(argThat(role -> "USER".equals(role.getName()) && "ROLE_USER".equals(role.getCode())));
  }

  @Test
  void testInitRolesWhenRolesAlreadyExist() {
    // Dado: Los roles ya existen
    when(roleRepository.findByCode("ROLE_ADMIN")).thenReturn(Optional.of(new Role("ADMIN", "ROLE_ADMIN")));
    when(roleRepository.findByCode("ROLE_USER")).thenReturn(Optional.of(new Role("USER", "ROLE_USER")));

    // Cuando
    roleInitializer.initRoles();

    // Entonces: No se crean nuevos roles
    verify(roleRepository, times(1)).findByCode("ROLE_ADMIN");
    verify(roleRepository, times(1)).findByCode("ROLE_USER");
    verify(roleRepository, never()).save(any(Role.class));
  }
}
