package com.example.gimnasiougr.Models;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum TipoClase {
    UNO("Tipo 1"),
    DOS("Tipo 2"),
    TRES("Tipo 3");
    private final String tipo;
}
