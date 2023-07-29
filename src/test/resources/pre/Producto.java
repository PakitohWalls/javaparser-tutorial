package com.tutorial.example;

public class Producto {

    private String nombre;
    private double precio;
    private int cantidad;

    public Producto(String nombre, double precio, int cantidad) {
        this.nombre = nombre;
        this.precio = precio;
        this.cantidad = cantidad;
    }

    public double calcularPrecioTotal() {
        double total = precio * cantidad;

        if (total > 100) {
            total = total * 0.9;
        }

        return total;
    }
}