package com.kush.lib.location.services;

import java.util.List;

import com.kush.lib.location.api.Location;
import com.kush.lib.location.api.Route;
import com.kush.service.BaseService;
import com.kush.service.annotations.Service;

@Service
public class LocationMapService extends BaseService {

    public Route getRoute(List<Location> asList) {
        return new Route();
    }
}
