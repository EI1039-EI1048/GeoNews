package es.uji.geonews.model.dao;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import es.uji.geonews.model.Location;
import es.uji.geonews.model.data.Data;
import es.uji.geonews.model.managers.LocationManager;
import es.uji.geonews.model.managers.ServiceManager;
import es.uji.geonews.model.services.Service;
import es.uji.geonews.model.services.ServiceName;

public class UserDao {
    int userId;
    Map<String, Location> locations;
    Map<String, Location> favoriteLocations;
    Map<String, Service> services;
    Map<String, List<String>> locationServices;
    Map<String, Map<String, Data>> lastData;
    String lastModification;    /// Aqui si nos interesa el horas minutos y segundos

    public UserDao() {}

    public UserDao (int userId, LocationManager locationManager, ServiceManager serviceManager) {
        this.userId = userId;
        this.locations = convertHashKeyToString(locationManager.getLocations()); ;
        this.favoriteLocations = convertHashKeyToString(locationManager.getFavoriteLocations());
        this.services = convertServiceNameToString(serviceManager.getServiceMap());
        this.locationServices =  convertLocationServices(serviceManager.getLocationServices());
        this.lastData = convertLastData(serviceManager.getLastData());
        this.lastModification = LocalDateTime.now().toString();
    }

    private Map<String, Map<String, Data>> convertLastData(Map<Integer, Map<ServiceName, Data>> lastData) {
        Map<String, Map<String, Data>> convertedLastData = new HashMap<>();
        for (Map.Entry<Integer, Map<ServiceName, Data>> entry: lastData.entrySet()) {
            convertedLastData.put(String.valueOf(entry.getKey()), convertServiceNameToString(entry.getValue()));
        }
        return convertedLastData;
    }

    private Map<String, List<String>> convertLocationServices(Map<Integer, List<ServiceName>> locationServices) {
        Map<String, List<String>> convertedMap = new HashMap<>();
        for (Map.Entry<Integer, List<ServiceName>> entry : locationServices.entrySet()) {
            List<String> servicesString = new ArrayList<>();
            for (ServiceName s: entry.getValue()) {
                servicesString.add(s.name);
            }
            convertedMap.put(String.valueOf(entry.getKey()), servicesString);
        }
        return convertedMap;
    }

    private <T> Map<String, T> convertHashKeyToString(Map<Integer, T> values) {
        Map<String, T> convertedMap = new HashMap<>();
        for (Integer key : values.keySet()) {
            convertedMap.put(String.valueOf(key), values.get(key));
        }
        return convertedMap;
    }

    private <T> Map<String, T> convertServiceNameToString(Map<ServiceName, T> map) {
        Map<String, T> convertedMap = new HashMap<>();
        for (ServiceName key : map.keySet()) {
            convertedMap.put(key.name, map.get(key));
        }
        return convertedMap;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public Map<String, Location> getLocations() {
        return locations;
    }

    public void setLocations(Map<String, Location> locations) {
        this.locations = locations;
    }

    public Map<String, Location> getFavoriteLocations() {
        return favoriteLocations;
    }

    public void setFavoriteLocations(Map<String, Location> favoriteLocations) {
        this.favoriteLocations = favoriteLocations;
    }

    public Map<String, Service> getServices() {
        return services;
    }

    public void setServices(Map<String, Service> services) {
        this.services = services;
    }

    public Map<String, List<String>> getLocationServices() {
        return locationServices;
    }

    public void setLocationServices(Map<String, List<String>> locationServices) {
        this.locationServices = locationServices;
    }

    public Map<String, Map<String, Data>> getLastData() {
        return lastData;
    }

    public void setLastData(Map<String, Map<String, Data>> lastData) {
        this.lastData = lastData;
    }

    public String getLastModification() {
        return lastModification;
    }

    public void setLastModification(String lastModification) {
        this.lastModification = lastModification;
    }
}
