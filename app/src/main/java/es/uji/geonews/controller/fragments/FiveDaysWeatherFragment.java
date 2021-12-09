package es.uji.geonews.controller.fragments;

import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import es.uji.geonews.R;
import es.uji.geonews.controller.FiveDaysForecastAdapter;
import es.uji.geonews.controller.tasks.GetFiveDayForecastData;
import es.uji.geonews.model.data.Data;
import es.uji.geonews.model.data.OpenWeatherForecastData;

public class FiveDaysWeatherFragment extends Fragment {
    private int locationId;

    public FiveDaysWeatherFragment(int locationId) {
        // Required empty public constructor
        this.locationId = locationId;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        RelativeLayout settings =  getActivity().findViewById(R.id.settings);
        settings.setVisibility(View.VISIBLE);

        View view = inflater.inflate(R.layout.fragment_five_days_weather, container, false);
        ConstraintLayout layout = view.findViewById(R.id.fiveDaysLayout);
        layout.setBackground(getSeasonBackground());
        RecyclerView recyclerView = view.findViewById(R.id.five_days_recycler_view);
        ProgressBar progressBar = view.findViewById(R.id.my_progress_bar);

        List<Data> forecast = new ArrayList<>();
        recyclerView.setAdapter(new FiveDaysForecastAdapter(forecast));
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        new GetFiveDayForecastData(locationId, recyclerView, progressBar, getContext()).execute();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    private Drawable getSeasonBackground() {
        final int DAY_SPRING_MIN = 80;
        final int DAY_SPRING_MAX = 172;
        final int DAY_SUMMER_MIN = DAY_SPRING_MAX;
        final int DAY_SUMMER_MAX = 264;
        final int DAY_FALL_MIN = DAY_SUMMER_MAX;
        final int DAY_FALL_MAX = 355;

        int dayOfYear = LocalDate.now().getDayOfYear();

        if (dayOfYear > DAY_SPRING_MIN && dayOfYear <= DAY_SPRING_MAX) {
            return AppCompatResources.getDrawable(getContext(), R.mipmap.spring);
        } else if (dayOfYear > DAY_SUMMER_MIN && dayOfYear <= DAY_SUMMER_MAX) {
            return AppCompatResources.getDrawable(getContext(), R.mipmap.summer);
        } else if (dayOfYear > DAY_FALL_MIN && dayOfYear <= DAY_FALL_MAX) {
            return AppCompatResources.getDrawable(getContext(), R.mipmap.autumn);
        }
        return AppCompatResources.getDrawable(getContext(), R.mipmap.winter);
    }
}