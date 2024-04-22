package com.example.thewind.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.thewind.data.forecastModels.ForecastData;
import com.example.thewind.databinding.RvItemLayoutBinding;
import com.squareup.picasso.Picasso;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class RvAdapter extends RecyclerView.Adapter<RvAdapter.ViewHolder> {
    private ArrayList<ForecastData> forecastArray;

    public RvAdapter(ArrayList<ForecastData> forecastArray) {
        this.forecastArray = forecastArray;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public RvItemLayoutBinding binding;

        public ViewHolder(RvItemLayoutBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        RvItemLayoutBinding itemBinding = RvItemLayoutBinding.inflate(layoutInflater, parent, false);
        return new ViewHolder(itemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ForecastData currentItem = forecastArray.get(position);
        RvItemLayoutBinding binding = holder.binding;
        String imageIcon = currentItem.getWeather().get(0).getIcon();
        String imageUri = "https://openweathermap.org/img/wn/" + imageIcon + ".png";

        Picasso.get().load(imageUri).into(binding.imgItem);

        binding.tvItemTemp.setText(currentItem.getMain().getTemp() + "°C");
        binding.tvItemStatus.setText(currentItem.getWeather().get(0).getDescription());
        binding.tvItemTime.setText(displayTime(currentItem.getDtTxt()));
    }

    private CharSequence displayTime(String dtTxt) {
        DateTimeFormatter input = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        DateTimeFormatter output = DateTimeFormatter.ofPattern("MM-dd HH:mm");
        LocalDateTime dateTime = LocalDateTime.parse(dtTxt, input);
        return output.format(dateTime);
    }

    @Override
    public int getItemCount() {
        return forecastArray.size();
    }
}
