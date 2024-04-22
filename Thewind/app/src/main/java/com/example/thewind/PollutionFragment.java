package com.example.thewind;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.example.thewind.databinding.FragmentPollutionBinding;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import java.util.ArrayList;
import java.util.List;

public class PollutionFragment extends Fragment {

    private FragmentPollutionBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentPollutionBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Bundle data = getArguments();
        List<String> pollutants = new ArrayList<String>() {{
            add("co");
            add("nh3");
            add("no");
            add("no2");
            add("o3");
            add("pm10");
            add("pm2_5");
            add("so2");
        }};

        ArrayList<BarEntry> list = new ArrayList<>();
        StringBuilder txtBuilder = new StringBuilder();

        for (int i = 0; i < pollutants.size(); i++) {
            String key = pollutants.get(i);
            String label;
            switch (key) {
                case "co":
                    label = "CO";
                    break;
                case "nh3":
                    label = "NH3";
                    break;
                case "no":
                    label = "NO";
                    break;
                case "no2":
                    label = "NO2";
                    break;
                case "o3":
                    label = "O3";
                    break;
                case "pm10":
                    label = "PM10";
                    break;
                case "pm2_5":
                    label = "PM2_5";
                    break;
                case "so2":
                    label = "SO2";
                    break;
                default:
                    label = "";
                    break;
            }

            Double value = data != null ? data.getDouble(key) : null;
            if (value != null) {
                list.add(new BarEntry(i + 1, value.floatValue()));
            }
            txtBuilder.append(label).append(": ").append(value != null ? value.toString() : "-").append("\n");
        }

        binding.textView.setText(txtBuilder.toString());

        BarDataSet barDataSet = new BarDataSet(list, "Pollutants");
        binding.barChart.setData(new BarData(barDataSet));

        barDataSet.setColors(ColorTemplate.VORDIPLOM_COLORS);
        barDataSet.setValueTextColor(Color.BLACK);
        barDataSet.setBarBorderColor(Color.BLACK);
        barDataSet.setBarBorderWidth(1f);

        binding.barChart.getDescription().setText("Air Pollutants");
        binding.barChart.animateY(1000);

        final String[] quarters = {"", "CO", "NH3", "NO", "NO2", "PM10", "O3", "PM2_5", "SO2"};
        ValueFormatter formatter = new ValueFormatter() {
            @Override
            public String getAxisLabel(float value, AxisBase axis) {
                return quarters[(int) value];
            }
        };
        binding.barChart.getXAxis().setValueFormatter(formatter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
