package es.uji.geonews.acceptance.R2;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import es.uji.geonews.model.Location;
import es.uji.geonews.model.LocationManager;
import es.uji.geonews.model.exceptions.NoLocationRegisteredException;
import es.uji.geonews.model.exceptions.NotValidCoordinatesException;
import es.uji.geonews.model.exceptions.ServiceNotAvailableException;
import es.uji.geonews.model.exceptions.UnrecognizedPlaceNameException;
import es.uji.geonews.model.services.GeocodeService;
import es.uji.geonews.model.services.OpenWeatherService;
import es.uji.geonews.model.services.Service;
import es.uji.geonews.model.services.ServiceManager;

public class R2_HU02_1 {
    private LocationManager locationManager;
    private ServiceManager serviceManager;
    private List<Location> activeList;
    private List<Location> nonActiveList;

    @Before
    public void init(){
        // Given
        GeocodeService geocode = new GeocodeService();
        serviceManager = new ServiceManager();
        serviceManager.addService(geocode);
        OpenWeatherService openWeatherService = new OpenWeatherService();
        serviceManager.addService(openWeatherService);
        locationManager = new LocationManager(geocode);
        activeList = new ArrayList<Location>();
    }

    @Test
    public void checkService_OneActivation_True()
            throws NotValidCoordinatesException, ServiceNotAvailableException, UnrecognizedPlaceNameException {
        // Given
        Location castellon = locationManager.addLocation("Castelló de la Plana");
        // When
        boolean confirmation = serviceManager.addServiceToLocation("OpenWeather", castellon);
        serviceManager.initLocationServices(castellon);

        // Then
        assertEquals(1, serviceManager.getServicesOfLocation(castellon.getId()).size());
        assertTrue( serviceManager.getServicesOfLocation(castellon.getId()).contains("OpenWeather"));
        assertTrue(confirmation);

    }
    @Test
    public void checkService_OneActivation_alreadyActive_False()
            throws NotValidCoordinatesException, ServiceNotAvailableException, UnrecognizedPlaceNameException {
        // Given
        Location castellon = locationManager.addLocation("Castelló de la Plana");
        int id = castellon.getId();
        serviceManager.addServiceToLocation("OpenWeather", castellon);
        serviceManager.initLocationServices(castellon);

        // When
        boolean confirmation = serviceManager.addServiceToLocation("OpenWeather", castellon);
        // Then
        assertEquals(1, serviceManager.getServicesOfLocation(id).size());
        assertTrue( serviceManager.getServicesOfLocation(id).contains("OpenWeather"));
        assertFalse(confirmation);
    }


}
