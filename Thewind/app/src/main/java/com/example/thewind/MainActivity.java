package com.example.thewind;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.widget.SearchView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.example.thewind.adapter.RvAdapter;
import com.example.thewind.data.forecastModels.ForecastData;
import com.example.thewind.databinding.ActivityMainBinding;
import com.example.thewind.databinding.BottomSheetLayoutBinding;
import com.example.thewind.utils.RetrofitInstance;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.squareup.picasso.Picasso;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private BottomSheetLayoutBinding sheetLayoutBinding;
    private BottomSheetDialog dialog;
    private PollutionFragment pollutionFragment;
    private String city = "jakarta";
    private FusedLocationProviderClient fusedLocationProviderClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        sheetLayoutBinding = BottomSheetLayoutBinding.inflate(getLayoutInflater());
        dialog = new BottomSheetDialog(this, R.style.BottomSheetTheme);
        dialog.setContentView(sheetLayoutBinding.getRoot());
        setContentView(binding.getRoot());

        pollutionFragment = new PollutionFragment();

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        binding.searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (query != null) {
                    city = query;
                }
                getCurrentWeather(city);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        fetchLocation();
        getCurrentWeather(city);

        binding.tvForecast.setOnClickListener(v -> OpenDialog());

        binding.tvLocation.setOnClickListener(v -> fetchLocation());
    }

    private void fetchLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        fusedLocationProviderClient.getLastLocation().addOnSuccessListener(location -> {
            Geocoder geocoder = new Geocoder(this, Locale.getDefault());
            try {
                List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    city = addresses.get(0).getLocality();
                } else {
                    city = addresses.get(0).getLocality();
                }
                getCurrentWeather(city);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    private void OpenDialog() {
        getForecast();

        sheetLayoutBinding.rvForecast.setHasFixedSize(true);
        sheetLayoutBinding.rvForecast.setLayoutManager(new GridLayoutManager(this, 1, RecyclerView.HORIZONTAL, false));

        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.show();
    }

    @SuppressLint("SetTextI18n")
    private void getForecast() {
        Call<ForecastData> call = RetrofitInstance.api.getForecast(city, "metric", getString(R.string.api_key));
        call.enqueue(new Callback<ForecastData>() {
            @Override
            public void onResponse(@NonNull Call<ForecastData> call, @NonNull Response<ForecastData> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ForecastData data = response.body();
                    ArrayList<ForecastData> forecastArray = new ArrayList<>(data.getList());
                    RvAdapter adapter = new RvAdapter(forecastArray);
                    sheetLayoutBinding.rvForecast.setAdapter(adapter);
                    sheetLayoutBinding.tvSheet.setText("Five days forecast in " + data.getCity().getName());
                }
            }

            @Override
            public void onFailure(@NonNull Call<ForecastData> call, @NonNull Throwable t) {
                Toast.makeText(getApplicationContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @SuppressLint("SetTextI18n")
    private void getCurrentWeather(String city) {
        Call<CurrentWeatherData> call = RetrofitInstance.api.getCurrentWeather(city, "metric", getString(R.string.api_key));
        call.enqueue(new Callback<CurrentWeatherData>() {
            @Override
            public void onResponse(@NonNull Call<CurrentWeatherData> call, @NonNull Response<CurrentWeatherData> response) {
                if (response.isSuccessful() && response.body() != null) {
                    CurrentWeatherData data = response.body();
                    String iconId = data.getWeather().get(0).getIcon();
                    String imgUrl = "https://openweathermap.org/img/wn/" + iconId + "@4x.png";
                    Picasso.get().load(imgUrl).into(binding.imgWeather);
                    binding.tvSunrise.setText(dateFormatConverter(data.getSys().getSunrise()));
                    binding.tvSunset.setText(dateFormatConverter(data.getSys().getSunset()));
                    binding.tvStatus.setText(data.getWeather().get(0).getDescription());
                    binding.tvWind.setText(data.getWind().getSpeed() + " KM/H");
                    binding.tvLocation.setText(data.getName() + "\n" + data.getSys().getCountry());
                    binding.tvTemp.setText(data.getMain().getTemp() + "°C");
                    binding.tvFeelsLike.setText("RealFeel: " + data.getMain().getFeelsLike() + "°C");
                    binding.tvMinTemp.setText("Min temp: " + data.getMain().getTempMin() + "°C");
                    binding.tvMaxTemp.setText("Max temp: " + data.getMain().getTempMax() + "°C");
                    binding.tvHumidity.setText(data.getMain().getHumidity() + "%");
                    binding.tvPressure.setText(data.getMain().getPressure() + "hPa");
                    binding.tvUpdateTime.setText("Last Update: " + dateFormatConverter(data.getDt()));
                    getPollution(data.getCoord().getLat(), data.getCoord().getLon());
                }
            }

            @Override
            public void onFailure(@NonNull Call<CurrentWeatherData> call, @NonNull Throwable t) {
                Toast.makeText(getApplicationContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getPollution(double lat, double lon) {
        Call<PollutionData> call = RetrofitInstance.api.getPollution(lat, lon, "metric", getString(R.string.api_key));
        call.enqueue(new Callback<PollutionData>() {
            @Override
            public void onResponse(@NonNull Call<PollutionData> call, @NonNull Response<PollutionData> response) {
                if (response.isSuccessful() && response.body() != null) {
                    PollutionData data = response.body();
                    int num = data.getList().get(0).getMain().getAqi();
                    switch (num) {
                        case 1:
                            binding.tvAirQual.setText(getString(R.string.good));
                            break;
                        case 2:
                            binding.tvAirQual.setText(getString(R.string.fair));
                            break;
                        case 3:
                            binding.tvAirQual.setText(getString(R.string.moderate));
                            break;
                        case 4:
                            binding.tvAirQual.setText(getString(R.string.poor));
                            break;
                        case 5:
                            binding.tvAirQual.setText(getString(R.string.very_poor));
                            break;
                        default:
                            binding.tvAirQual.setText("no data");
                            break;
                    }
                    binding.layoutPollution.setOnClickListener(v -> {
                        Bundle bundle = new Bundle();
                        PollutionComponents components = data.getList().get(0).getComponents();
                        bundle.putDouble("co", components.getCo());
                        bundle.putDouble("nh3", components.getNh3());
                        bundle.putDouble("no", components.getNo());
                        bundle.putDouble("no2", components.getNo2());
                        bundle.putDouble("o3", components.getO3());
                        bundle.putDouble("pm10", components.getPm10());
                        bundle.putDouble("pm2_5", components.getPm2_5());
                        bundle.putDouble("so2", components.getSo2());
                        pollutionFragment.setArguments(bundle);
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.frameLayout, pollutionFragment)
                                .addToBackStack(null)
                                .commit();
                    });
                }
            }

            @Override
            public void onFailure(@NonNull Call<PollutionData> call, @NonNull Throwable t) {
                Toast.makeText(getApplicationContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private String dateFormatConverter(long date) {
        return new SimpleDateFormat("hh:mm a", Locale.ENGLISH).format(new Date(date * 1000));
    }
}
