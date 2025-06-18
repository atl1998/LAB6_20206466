package com.example.lab6_20206466;

public class Linea1 {
    private String id;
    private String tarjetaId;
    private String fecha;
    private String estacionEntrada;
    private String estacionSalida;
    private String tiempoViaje;

    public Linea1() {
        // Requerido por Firebase
    }

    public Linea1(String id, String tarjetaId, String fecha, String estacionEntrada, String estacionSalida, String tiempoViaje) {
        this.id = id;
        this.tarjetaId = tarjetaId;
        this.fecha = fecha;
        this.estacionEntrada = estacionEntrada;
        this.estacionSalida = estacionSalida;
        this.tiempoViaje = tiempoViaje;
    }

    // Getters y setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getTarjetaId() { return tarjetaId; }
    public void setTarjetaId(String tarjetaId) { this.tarjetaId = tarjetaId; }

    public String getFecha() { return fecha; }
    public void setFecha(String fecha) { this.fecha = fecha; }

    public String getEstacionEntrada() { return estacionEntrada; }
    public void setEstacionEntrada(String estacionEntrada) { this.estacionEntrada = estacionEntrada; }

    public String getEstacionSalida() { return estacionSalida; }
    public void setEstacionSalida(String estacionSalida) { this.estacionSalida = estacionSalida; }

    public String getTiempoViaje() { return tiempoViaje; }
    public void setTiempoViaje(String tiempoViaje) { this.tiempoViaje = tiempoViaje; }
}

