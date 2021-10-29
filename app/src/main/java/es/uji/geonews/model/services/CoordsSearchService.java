package es.uji.geonews.model.services;


import java.io.IOException;

import es.uji.geonews.model.exceptions.ServiceNotAvailableException;
import es.uji.geonews.model.exceptions.UnrecognizedPlaceNameException;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONException;
import org.json.JSONObject;

import java.time.LocalDate;

import es.uji.geonews.model.GeographCoords;

public class CoordsSearchService extends Service  {

    private static final  String GEOCODE_API_KEY = "739559811684314511027x58957";
    private final OkHttpClient client;

    public CoordsSearchService() {
        super("Geocode", "Coordinates Search Service", LocalDate.now());
        client = new OkHttpClient();
    }

    public boolean isAvailable(){
        return true;
    }

    public GeographCoords getCoordsFrom(String placeName)
            throws UnrecognizedPlaceNameException, ServiceNotAvailableException {
        String url = "https://geocode.xyz/"+ placeName +"?json=1&auth=" + GEOCODE_API_KEY;
        Request request = new Request.Builder().url(url).build();
        final JSONObject jsonObject;
        GeographCoords geographCoords;

        try (Response response = client.newCall(request).execute()) {
            jsonObject = new JSONObject(response.body().string());
            if (jsonObject.has("error")){
               throw new UnrecognizedPlaceNameException();
            }
            double longt = jsonObject.getDouble("longt");
            double latt = jsonObject.getDouble("latt");
            geographCoords = new GeographCoords(latt, longt);
        } catch (IOException | JSONException exception){
            throw new ServiceNotAvailableException();
        }
        return geographCoords;
    }

    public String getPlaceNameFromCoords(GeographCoords coords) {
        String url = "https://geocode.xyz/"+ coords.toString() +"?json=1&auth=" + GEOCODE_API_KEY;
        Request request = new Request.Builder().url(url).build();
        final JSONObject jsonObject;
        String placeName = null;

        try (Response response = client.newCall(request).execute()) {
            jsonObject = new JSONObject(response.body().string());
            if (jsonObject.has("error")){
                return null;
            }
            placeName = jsonObject.getJSONObject("osmtags").getString("name_es");
        } catch (IOException | JSONException exception){
            exception.printStackTrace();
        }
        return placeName;
    }
}
