package es.uji.geonews.model.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import es.uji.geonews.model.Location;
import es.uji.geonews.model.data.Data;
import es.uji.geonews.model.exceptions.ServiceNotAvailableException;

public class ServiceManager {

    private final Map<String, Service> serviceMap;
    private final Map<Integer, List<String>> locationServices;
    private final ContextDataGetter contextDataGetter;


    public ServiceManager(){
        this.serviceMap = new HashMap<>();
        this.locationServices = new HashMap<>();
        this.contextDataGetter = new ContextDataGetter();
    }

    public List<Service> getServices(){
        return new ArrayList<>(serviceMap.values());
    }

    public List<ServiceHttp> getHttpServices(){
        List<ServiceHttp> httpServices = new ArrayList<>();
        for(Service service: serviceMap.values()){
            if(service instanceof ServiceHttp){
                httpServices.add((ServiceHttp) service);
            }
        }
        return httpServices;
    }

    public Service getService(String serviceName) {
        return serviceMap.get(serviceName);
    }

    public void addService(Service service){
        serviceMap.put(service.getServiceName(), service);
    }


    public Data getData(String serviceName, Location location) throws ServiceNotAvailableException {
        List<String> activeServices = locationServices.get(location.getId());
        if (location != null && activeServices.contains(serviceName)) {
            DataGetterStrategy service = (DataGetterStrategy) getService(serviceName);
            contextDataGetter.setService(service);
            return contextDataGetter.getData(location);
        }
        return null;
    }


    public List<String> validateLocation(Location location){
        List<String> services = new ArrayList<>();
        for(ServiceHttp service: getHttpServices()){
            if(!service.getServiceName().equals("Geocode") && service.isActive && service.validateLocation(location)){
                services.add(service.getServiceName());
            }
        }
        return services;
    }

    public boolean addServiceToLocation(String serviceName, Location location) {
        if (location != null) {
            int locationId = location.getId();
            List<String> currentServicesInLocation = locationServices.get(locationId);
            if (!currentServicesInLocation.contains(serviceName)) {
                currentServicesInLocation.add(serviceName); // TODO: Check if instance of HTTPService
                locationServices.put(locationId, currentServicesInLocation);
                return true;
            }
        }
        return false;
    }

    public boolean removeServiceFromLocation(String serviceName, Location location) {
        if (location != null) {
            int locationId = location.getId();
            List<String> currentServicesInLocation = locationServices.get(locationId);
            if (currentServicesInLocation != null) {
                if (currentServicesInLocation.contains(serviceName)) {
                    currentServicesInLocation.remove(serviceName);
                    return true;
                }
            }
        }
        return false;
    }

    public List<String> getServicesOfLocation(int locationId) {
        List<String> servicesOfLocation = locationServices.get(locationId);
        if (servicesOfLocation == null)
            return new ArrayList<>();
        return servicesOfLocation;
    }

    public void deactivateService(String serviceName) {
        Service service = getService(serviceName);
        if (service != null) service.deactivate();
    }

    public void initLocationServices(Location newLocation) {
        locationServices.put(newLocation.getId(), new ArrayList<>());
    }
}
