package es.uji.geonews.controller.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import es.uji.geonews.R;
import es.uji.geonews.controller.tasks.GetAirVisualData;
import es.uji.geonews.model.managers.GeoNewsManager;
import es.uji.geonews.model.managers.GeoNewsManagerSingleton;


public class AirVisualFragment extends Fragment {
    private GeoNewsManager geoNewsManager;

    public AirVisualFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        geoNewsManager = GeoNewsManagerSingleton.getInstance(getContext());

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_air_visual, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RelativeLayout settings =  getActivity().findViewById(R.id.settings);
        settings.setVisibility(View.VISIBLE);

        ProgressBar progressBar = view.findViewById(R.id.my_progress_bar);
        TextView temperatureOutput = view.findViewById(R.id.temperatureOutput);
        TextView humidityOutput = view.findViewById(R.id.humidityOutput);
        TextView pressureOutput = view.findViewById(R.id.pressureOutput);
        Button actualizaButton = view.findViewById(R.id.newsButton);

        actualizaButton.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                // Show the dialog
                new GetAirVisualData(geoNewsManager, temperatureOutput, pressureOutput, humidityOutput, progressBar).execute();
            }
        });
    }
}