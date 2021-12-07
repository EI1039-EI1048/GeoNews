package es.uji.geonews.controller;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import es.uji.geonews.R;
import es.uji.geonews.controller.fragments.OnItemClickListener;
import es.uji.geonews.controller.tasks.UpdateFavorites;
import es.uji.geonews.model.Location;
import es.uji.geonews.model.exceptions.NoLocationRegisteredException;
import es.uji.geonews.model.managers.GeoNewsManager;
import es.uji.geonews.model.managers.GeoNewsManagerSingleton;


public class LocationViewHolder extends RecyclerView.ViewHolder {
    private final TextView mainNameOutput;
    private final TextView subnameOutput;
    private final ImageView favourite;
    private final ImageView info;

    public LocationViewHolder(View itemView) {
        super(itemView);
        mainNameOutput = itemView.findViewById(R.id.main_name_output);
        subnameOutput =  itemView.findViewById(R.id.subname_output);
        favourite =  itemView.findViewById(R.id.add_to_favorites_button);
        info =  itemView.findViewById(R.id.location_information_button);
    }

    public void bind(Location location, OnItemClickListener listener) {

        setLocationTitleAndSubtitle(location);

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onItemClick(location);
            }
        });

        info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putInt("locationId", location.getId());
                NavController navController = Navigation.findNavController(view);
                GeoNewsManager geoNewsManager = GeoNewsManagerSingleton.getInstance(view.getContext());
                try {
                    if(geoNewsManager.getLocation(location.getId()).isActive()){
                        navController.navigate(R.id.activeLocationInfoFragment, bundle);
                    } else {
                        navController.navigate(R.id.nonActiveLocationInfoFragment, bundle);
                    }
                } catch (NoLocationRegisteredException e) {
                    e.printStackTrace();
                }
            }
        });

        favourite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new UpdateFavorites(itemView.getContext(), location, itemView).execute();

            }
        });
    }

    private void setLocationTitleAndSubtitle(Location location){
        String mainName;
        String subname;
        if (! location.getAlias().equals("")) {     // If location has alias
            mainName = location.getAlias();
            if (location.getPlaceName() != null) subname = location.getPlaceName();
            else subname = location.getGeographCoords().toString();
        } else {                                    // If location has no alias
            if (location.getPlaceName() != null) {
                mainName = location.getPlaceName();
                subname = location.getGeographCoords().toString();
            }
            else{
                mainName = location.getGeographCoords().toString();
                subname = "Topónimo desconocido";
            }
        }
        mainNameOutput.setText(mainName);
        subnameOutput.setText(subname);
    }
}

