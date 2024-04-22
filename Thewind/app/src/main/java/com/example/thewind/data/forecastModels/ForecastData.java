package com.example.thewind.data.forecastModels;

import java.util.List;

public class ForecastData {
    private Clouds clouds;
    private int dt;
    private String dt_txt;
    private Main main;
    private double pop;
    private Rain rain;
    private Sys sys;
    private int visibility;
    private List<Weather> weather;
    private Wind wind;

    public ForecastData(Clouds clouds, int dt, String dt_txt, Main main, double pop, Rain rain, Sys sys, int visibility, List<Weather> weather, Wind wind) {
        this.clouds = clouds;
        this.dt = dt;
        this.dt_txt = dt_txt;
        this.main = main;
        this.pop = pop;
        this.rain = rain;
        this.sys = sys;
        this.visibility = visibility;
        this.weather = weather;
        this.wind = wind;
    }

    public Clouds getClouds() {
        return clouds;
    }

    public int getDt() {
        return dt;
    }

    public String getDt_txt() {
        return dt_txt;
    }

    public Main getMain() {
        return main;
    }

    public double getPop() {
        return pop;
    }

    public Rain getRain() {
        return rain;
    }

    public Sys getSys() {
        return sys;
    }

    public int getVisibility() {
        return visibility;
    }

    public List<Weather> getWeather() {
        return weather;
    }

    public Wind getWind() {
        return wind;
    }
}
