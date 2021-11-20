package es.uji.geonews.acceptance.R2;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import es.uji.geonews.model.Location;
import es.uji.geonews.model.managers.GeoNewsManager;
import es.uji.geonews.model.exceptions.NoLocationRegisteredException;
import es.uji.geonews.model.exceptions.NotValidCoordinatesException;
import es.uji.geonews.model.exceptions.ServiceNotAvailableException;
import es.uji.geonews.model.exceptions.UnrecognizedPlaceNameException;
import es.uji.geonews.model.services.ServiceName;

public class HU06_2 {
    private GeoNewsManager geoNewsManager;

    @Before
    public void init(){
        geoNewsManager = new GeoNewsManager();
    }

    @Test
    public void reactivateLocation_availableServices_true()
            throws NotValidCoordinatesException, ServiceNotAvailableException, UnrecognizedPlaceNameException, NoLocationRegisteredException {
        // Given
        Location valencia = geoNewsManager.addLocation("Valencia");
        geoNewsManager.activateLocation(valencia.getId());
        geoNewsManager.deactivateLocation(valencia.getId());

        // When
        boolean result = geoNewsManager.activateLocation(valencia.getId());

        // Then
        assertTrue(result);
    }

    @Test
    public void reactivateLocation_anyServices_false()
            throws NotValidCoordinatesException, ServiceNotAvailableException, UnrecognizedPlaceNameException, NoLocationRegisteredException {
        // Given
        Location castellon = geoNewsManager.addLocation("Castelló de la Plana");
        geoNewsManager.activateLocation(castellon.getId());

        Location valencia = geoNewsManager.addLocation("Valencia");
        geoNewsManager.activateLocation(valencia.getId());
        geoNewsManager.deactivateLocation(valencia.getId());
        geoNewsManager.deactivateService(ServiceName.AIR_VISUAL);
        geoNewsManager.deactivateService(ServiceName.OPEN_WEATHER);
        geoNewsManager.deactivateService(ServiceName.CURRENTS);

        // When
        boolean result = geoNewsManager.activateLocation(valencia.getId());

        // Then
        assertFalse(result);
        assertEquals(1, geoNewsManager.getActiveLocations().size());
    }
}
