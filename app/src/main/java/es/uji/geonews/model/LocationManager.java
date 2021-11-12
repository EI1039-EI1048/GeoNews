package es.uji.geonews.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import es.uji.geonews.model.data.Data;
import es.uji.geonews.model.exceptions.NoLocationRegisteredException;
import es.uji.geonews.model.exceptions.NotValidCoordinatesException;
import es.uji.geonews.model.exceptions.ServiceNotAvailableException;
import es.uji.geonews.model.exceptions.UnrecognizedPlaceNameException;
import es.uji.geonews.model.services.GeocodeService;
import es.uji.geonews.model.services.DataGetterStrategy;
import es.uji.geonews.model.services.Service;
import es.uji.geonews.model.services.ServiceHttp;
import es.uji.geonews.model.services.ServiceManager;

public class LocationManager {
    private final Map<Integer, Location> locations;
    private final Map<Integer, Location> favoriteLocations;
    private final Map<Integer, List<String>> locationServices;
    private final ServiceManager serviceManager;
    private LocationCreator locationCreator;

    public LocationManager(ServiceManager serviceManager) {
        this.serviceManager = serviceManager;
        this.locations = new HashMap<>();
        this.favoriteLocations = new HashMap<>();
        this.locationServices = new HashMap<>();
        GeocodeService geocodeService = (GeocodeService) serviceManager.getService("Geocode");
        this.locationCreator = new LocationCreator(geocodeService);
    }

    public List<Location> getActiveLocations() {
        List<Location> activeLocations = new ArrayList<>();
        for (Location location: locations.values()){
            if (location.isActive()) activeLocations.add(location);
        }
        return activeLocations;
    }

    public List<Location> getNonActiveLocations() throws NoLocationRegisteredException {
        if (locations.isEmpty()) {
            throw new NoLocationRegisteredException();
        }

        List<Location> nonActiveLocations = new ArrayList<>();
        for (Location location: locations.values()){
            if (!location.isActive()) nonActiveLocations.add(location);
        }
        return nonActiveLocations;
    }

    public List<Location> getFavouriteLocations() throws NoLocationRegisteredException {
        if(favoriteLocations.size() == 0 && locations.size() == 0) throw new NoLocationRegisteredException();
        return new ArrayList<>(favoriteLocations.values());
    }

    public Location addLocation(String string) throws UnrecognizedPlaceNameException,
            ServiceNotAvailableException, NotValidCoordinatesException {
        Location location = locationCreator.createLocation(string);
        if (location!= null) {
            locations.put(location.getId(), location);
            locationServices.put(location.getId(), new ArrayList<>());
        }
       return location;
    }

    public boolean removeLocation(int locationId){
        Location location = locations.get(locationId);
        if (location != null && !location.isActive()){
            locations.remove(locationId);
            locationServices.remove(locationId);
            return true;
        }
        return false;
    }

    public boolean addToFavorites(int locationId){
        Location location = locations.get(locationId);
        if (location != null){
            favoriteLocations.put(locationId, location);
            locations.remove(locationId);
            return true;
        }
        return false;
    }

    public boolean removeFromFavorites(int locationId){
        Location location = favoriteLocations.get(locationId);
        if (location != null){
            locations.put(locationId, location);
            favoriteLocations.remove(locationId);
            return true;
        }
        return false;
    }

    public List<String> validateLocation(int locationId){
        Location location = locations.get(locationId);
        List<String> services = new ArrayList<>();
        for(ServiceHttp service: serviceManager.getHttpServices()){
            if(!service.getServiceName().equals("Geocode") && service.validateLocation(location)){
                services.add(service.getServiceName());
            }
        }
        return services;
    }

    public boolean setAliasToLocation(String alias, int locationId) throws NoLocationRegisteredException {
        //Check if alias is already asign to any other locaton
        if (checkIfExistAlias(alias)) return false;

        //Check if location is in active and inactive locations
        Location location = getLocation(locationId);

        if (alias != null && !alias.equals("")) {
            location.setAlias(alias);
            return true;
        }
        return false;
    }

    private boolean checkIfExistAlias(String newAlias){
        for(Location location: locations.values()){
            if (location.getAlias().equals(newAlias)){
                return true;
            }
        }
        return false;
    }

    public Location getLocation(int idLocation) throws NoLocationRegisteredException {
        Location location = locations.get(idLocation);
        if (location == null) {
            throw new NoLocationRegisteredException();
        }
        return location;
    }

    public boolean activateLocation(int id) throws NoLocationRegisteredException {
        List<String> services = validateLocation(id);
        Location location = getLocation(id);
        if (location != null && !location.isActive() && services.size() > 0) {
            getLocation(id).activate();
            return true;
        }
        return false;
    }

    public boolean deactivateLocation(int id) {
        Location location = locations.get(id);
        if (location != null && location.isActive()){
            return location.deactivate();
        }
        return false;
    }

    public Service getService(String serviceName) {
        return serviceManager.getService(serviceName);
    }

    public Data getData(String serviceName, int locationId) throws ServiceNotAvailableException {
        Location location = locations.get(locationId);
        if (location != null && locationServices.get(locationId).contains(serviceName)) {
            DataGetterStrategy service = (DataGetterStrategy) serviceManager.getService(serviceName);
            return serviceManager.getData(service, location);
        }
        return null;
    }

    public boolean addServiceToLocation(String serviceName, int locationId) {
        Location location = locations.get(locationId);
        if (location != null) {
            if (!locationServices.get(locationId).contains(serviceName)) {
                List<String> currentServicesInLocation = locationServices.get(locationId);
                currentServicesInLocation.add(serviceName);
                locationServices.put(locationId, currentServicesInLocation);
                return true;
            }
        }
        return false;
    }
    public boolean removeServiceFromLocation(String serviceName, int locationId) {
        Location location = locations.get(locationId);
        if (location != null) {
            if (locationServices.get(locationId).contains(serviceName)) {
                locationServices.get(locationId).remove(serviceName);
                return true;
            }
        }
        return false;
    }

    public List<String> getLocationService(int locationId){
        return locationServices.get(locationId);
    }

    public void deactivateService(String serviceName) {
        serviceManager.removeService(serviceName);
    }
}

