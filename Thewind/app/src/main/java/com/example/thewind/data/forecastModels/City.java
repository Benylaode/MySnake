package com.example.thewind.data.forecastModels;

public class City {
    private Coord coord;
    private String country;
    private int id;
    private String name;
    private int population;
    private int sunrise;
    private int sunset;
    private int timezone;

    public City(Coord coord, String country, int id, String name, int population, int sunrise, int sunset, int timezone) {
        this.coord = coord;
        this.country = country;
        this.id = id;
        this.name = name;
        this.population = population;
        this.sunrise = sunrise;
        this.sunset = sunset;
        this.timezone = timezone;
    }

    public Coord getCoord() {
        return coord;
    }

    public String getCountry() {
        return country;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getPopulation() {
        return population;
    }

    public int getSunrise() {
        return sunrise;
    }

    public int getSunset() {
        return sunset;
    }

    public int getTimezone() {
        return timezone;
    }
}
