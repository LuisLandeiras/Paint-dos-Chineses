package com.example.designprogram;

public class Coordenadas {
    private double x;
    private double y;

    public Coordenadas(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public Coordenadas() {
        this.x = 0;
        this.y = 0;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }
}
