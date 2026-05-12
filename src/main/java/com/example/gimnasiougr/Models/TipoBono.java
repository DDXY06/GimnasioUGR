package com.example.gimnasiougr.Models;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum TipoBono {
    UNO("Tipo 1"),
    DOS("Tipo 2");
    private final String nombre;
}