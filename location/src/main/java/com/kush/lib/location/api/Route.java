package com.kush.lib.location.api;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

public class Route {

    private final Place start;
    private final Place end;
    private Route remainingRoute;

    public Route(Place start, Place end, Route remainingRoute) {
        this.start = start;
        this.end = end;
        this.remainingRoute = remainingRoute;
    }

    public Place getStart() {
        return start;
    }

    public Place getEnd() {
        return end;
    }

    public Optional<Route> getRemainingRoute() {
        return remainingRoute == null ? Optional.empty() : Optional.of(remainingRoute);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        int current = 1;
        Route currentRoute = this;
        while (currentRoute != null) {
            builder.append(current).append('.').append(' ');
            builder.append(currentRoute.start.getName()).append(" -> ").append(currentRoute.end.getName());
            builder.append('\n');
            current++;
            currentRoute = currentRoute.getRemainingRoute().isPresent() ? currentRoute.getRemainingRoute().get() : null;
        }
        return builder.toString();
    }

    private void setRemainingRoute(Route remainingRoute) {
        this.remainingRoute = remainingRoute;
    }

    public static class RouteBuilder {

        private final List<Place> places = new LinkedList<>();

        public RouteBuilder add(Place place) {
            places.add(place);
            return this;
        }

        public Route build() {
            Place source = places.remove(0);
            Place current = source;
            Route lastRoute = null;
            Route finalRoute = null;
            for (Place place : places) {
                Place destination = place;
                Route route = new Route(current, destination, null);
                current = destination;
                if (finalRoute == null) {
                    finalRoute = route;
                }
                if (lastRoute != null) {
                    lastRoute.setRemainingRoute(route);
                }
                lastRoute = route;
            }
            return finalRoute;
        }
    }
}
