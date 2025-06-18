package com.example.lab6_20206466;

public class LimaPass {
    private String id;
    private String tarjetaId;
    private String fecha;
    private String paraderoEntrada;
    private String paraderoSalida;
    private String tiempoViaje;

    public LimaPass() {
        // Requerido por Firebase
    }

    public LimaPass(String id, String tarjetaId, String fecha, String paraderoEntrada, String paraderoSalida, String tiempoViaje) {
        this.id = id;
        this.tarjetaId = tarjetaId;
        this.fecha = fecha;
        this.paraderoEntrada = paraderoEntrada;
        this.paraderoSalida = paraderoSalida;
        this.tiempoViaje = tiempoViaje;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTarjetaId() {
        return tarjetaId;
    }

    public void setTarjetaId(String tarjetaId) {
        this.tarjetaId = tarjetaId;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getParaderoEntrada() {
        return paraderoEntrada;
    }

    public void setParaderoEntrada(String paraderoEntrada) {
        this.paraderoEntrada = paraderoEntrada;
    }

    public String getParaderoSalida() {
        return paraderoSalida;
    }

    public void setParaderoSalida(String paraderoSalida) {
        this.paraderoSalida = paraderoSalida;
    }

    public String getTiempoViaje() {
        return tiempoViaje;
    }

    public void setTiempoViaje(String tiempoViaje) {
        this.tiempoViaje = tiempoViaje;
    }
}
