package com.example.thewind.data.forecastModels;

public class Weather {
    private String description;
    private String icon;
    private int id;
    private String main;

    public Weather(String description, String icon, int id, String main) {
        this.description = description;
        this.icon = icon;
        this.id = id;
        this.main = main;
    }

    public String getDescription() {
        return description;
    }

    public String getIcon() {
        return icon;
    }

    public int getId() {
        return id;
    }

    public String getMain() {
        return main;
    }
}
