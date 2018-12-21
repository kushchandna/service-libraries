package com.kush.lib.location.services;

import static java.util.Arrays.asList;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.kush.lib.location.api.Location;
import com.kush.lib.location.api.Route;
import com.kush.service.BaseServiceTest;

public class LocationMapServiceTest extends BaseServiceTest {

    private LocationMapService locationMapService;

    @Before
    public void beforeEachTest() throws Exception {
        locationMapService = new LocationMapService();
        registerService(locationMapService);
    }

    @Test
    public void preparePathToDestination() throws Exception {
        Location ionLocation = new Location(28.621625, 77.355413);
        Location kagLocation = new Location(28.635647, 77.367188);
        Location banglaSahibLocation = new Location(28.628334, 77.209429);

        Route route = locationMapService.getRoute(asList(ionLocation, kagLocation, banglaSahibLocation));
        List<Location> locations = route.getLocations();
        System.out.println(locations);
    }
}
