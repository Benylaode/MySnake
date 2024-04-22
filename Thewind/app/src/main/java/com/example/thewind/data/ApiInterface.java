package com.example.weatherapp.data;

import com.example.thewind.data.forecastModels.Forecast;
import com.example.thewind.data.models.CurrentWeather;
import com.example.thewind.data.pollutionModels.PollutionData;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiInterface {

    @GET("weather?")
    Response<CurrentWeather> getCurrentWeather(
        @Query("q") String city,
        @Query("units") String units,
        @Query("appid") String apiKey
    );

    @GET("forecast?")
    Response<Forecast> getForecast(
        @Query("q") String city,
        @Query("units") String units,
        @Query("appid") String apiKey
    );

    @GET("air_pollution?")
    Response<PollutionData> getPollution(
        @Query("lat") double lat,
        @Query("lon") double lon,
        @Query("units") String units,
        @Query("appid") String apiKey
    );
}
