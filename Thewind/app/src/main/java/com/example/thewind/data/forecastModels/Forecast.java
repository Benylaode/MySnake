package com.example.thewind.data.forecastModels;

import java.util.List;

public class Forecast {
    private City city;
    private int cnt;
    private String cod;
    private List<ForecastData> list;
    private int message;

    public Forecast(City city, int cnt, String cod, List<ForecastData> list, int message) {
        this.city = city;
        this.cnt = cnt;
        this.cod = cod;
        this.list = list;
        this.message = message;
    }

    public City getCity() {
        return city;
    }

    public int getCnt() {
        return cnt;
    }

    public String getCod() {
        return cod;
    }

    public List<ForecastData> getList() {
        return list;
    }

    public int getMessage() {
        return message;
    }
}
