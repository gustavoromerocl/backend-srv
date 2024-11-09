package com.duocuc.backend_srv.model;

public enum Difficulty {
  EASY("FÁCIL"),
  MEDIUM("MEDIANA"),
  HARD("DIFÍCIL");

  private final String displayName;

  // Constructor para asignar el valor a cada constante
  Difficulty(String displayName) {
    this.displayName = displayName;
  }

  // Método getter para obtener el valor asociado
  public String getDisplayName() {
    return displayName;
  }
}
