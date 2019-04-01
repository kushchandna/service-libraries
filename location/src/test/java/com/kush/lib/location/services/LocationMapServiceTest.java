package com.kush.lib.location.services;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;

import com.kush.lib.location.api.Location;
import com.kush.lib.location.api.Place;
import com.kush.lib.location.api.Route;
import com.kush.service.BaseServiceTest;

public class LocationMapServiceTest extends BaseServiceTest {

    private static final Logger LOG = LogManager.getFormatterLogger();

    @Test
    public void preparePathToDestination() throws Exception {
        Location ionLocation = new Location(28.621625, 77.355413);
        Place ionPlace = new Place("ION Trading", ionLocation, null);

        Location kagLocation = new Location(28.635647, 77.367188);
        Place kagPlace = new Place("Krishna Apra Gardens", kagLocation, null);

        Location banglaSahibLocation = new Location(28.628334, 77.209429);
        Place banglaSahibPlace = new Place("Bangla Sahib", banglaSahibLocation, null);

        Route route = new Route.RouteBuilder()
            .add(kagPlace)
            .add(ionPlace)
            .add(banglaSahibPlace)
            .add(kagPlace)
            .build();
        LOG.info("\n" + route);
    }
}
