package com.example.lab6_20206466;

public class Usuario {
    public String nombre;
    public String correo;

    public Usuario() {
        // Constructor vacío requerido por Firebase
    }

    public Usuario(String nombre, String correo) {
        this.nombre = nombre;
        this.correo = correo;
    }
}

