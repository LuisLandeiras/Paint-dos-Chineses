package com.example.designprogram;

public class Cordenadas {
    private double x;
    private double y;

    public Cordenadas(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public Cordenadas() {
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
