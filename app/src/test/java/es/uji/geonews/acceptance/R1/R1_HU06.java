package es.uji.geonews.acceptance.R1;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import java.util.List;

import es.uji.geonews.model.GeographCoords;
import es.uji.geonews.model.Location;
import es.uji.geonews.model.LocationManager;
import es.uji.geonews.model.exceptions.NoLocationRegisteredException;
import es.uji.geonews.model.exceptions.NotValidCoordinatesException;
import es.uji.geonews.model.exceptions.ServiceNotAvailableException;
import es.uji.geonews.model.exceptions.UnrecognizedPlaceNameException;
import es.uji.geonews.model.services.CoordsSearchService;
import es.uji.geonews.model.services.OpenWeatherService;
import es.uji.geonews.model.services.Service;
import es.uji.geonews.model.services.ServiceManager;

public class R1_HU06 {
    private LocationManager locationManager;

    @Before
    public void init() {
        ServiceManager serviceManager = new ServiceManager();
        Service GeoCode = new CoordsSearchService();
        Service OpenWeather = new OpenWeatherService();
        serviceManager.addService(GeoCode);
        serviceManager.addService(OpenWeather);
        locationManager = new LocationManager(serviceManager);
    }

    @Test
    public void activateLocation_KnownPlaceName_true()
            throws ServiceNotAvailableException, UnrecognizedPlaceNameException, NotValidCoordinatesException, NoLocationRegisteredException {

        Location newLocation = locationManager.addLocation("Castello de la Plana");
        locationManager.validateLocation(newLocation.getId());
        // When
        List<String> services = locationManager.activateLocation(newLocation.getId());
        // Then
        assertTrue(services.size() > 0);
        assertTrue(locationManager.getLocation(newLocation.getId()).isActive());
    }

    @Test
    public void activateLocation_UnKnownPlaceName_true()
            throws ServiceNotAvailableException, NotValidCoordinatesException, UnrecognizedPlaceNameException, NoLocationRegisteredException {
        Location castellon = locationManager.addLocation("Castello de la Plana");
        locationManager.validateLocation(castellon.getId());
        Location valencia = locationManager.addLocation("Valencia");
        locationManager.validateLocation(valencia.getId());
        locationManager.activateLocation(castellon.getId());
        locationManager.activateLocation(valencia.getId());

        GeographCoords coords = new GeographCoords(33.65001, -41.19001);
        Location newLocation = locationManager.addLocation(coords.toString());
        locationManager.validateLocation(newLocation.getId());
        // When
        List<String> services = locationManager.activateLocation(newLocation.getId());
        // Then
        assertTrue(services.size() > 0);
        assertTrue(locationManager.getLocation(newLocation.getId()).isActive());
    }

    @Test
    public void activateLocation_LocationAlreadyActive_false()
            throws NotValidCoordinatesException, ServiceNotAvailableException, UnrecognizedPlaceNameException {
        // Given
        Location castellon = locationManager.addLocation("Castello de la Plana");
        locationManager.validateLocation(castellon.getId());
        Location valencia = locationManager.addLocation("Valencia");
        locationManager.validateLocation(valencia.getId());
        locationManager.activateLocation(castellon.getId());
        locationManager.activateLocation(valencia.getId());

        Location location = locationManager.getActiveLocations().get(0);
        // When
        List<String> services = locationManager.activateLocation(location.getId());
        // Then
        assertFalse(services.size() > 0);
        assertEquals(2, locationManager.getActiveLocations().size());
    }
}
