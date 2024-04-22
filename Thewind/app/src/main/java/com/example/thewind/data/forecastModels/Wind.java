package com.example.thewind.data.forecastModels;

public class Wind {
    private int deg;
    private double gust;
    private double speed;

    public Wind(int deg, double gust, double speed) {
        this.deg = deg;
        this.gust = gust;
        this.speed = speed;
    }

    public int getDeg() {
        return deg;
    }

    public double getGust() {
        return gust;
    }

    public double getSpeed() {
        return speed;
    }
}
