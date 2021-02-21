package bguefano.parkings.domain;

import lombok.Data;

@Data(staticConstructor="between")
public class Distance {

    private static final double RADIUS = 6378137;

    final Coordinates x;

    final Coordinates y;

    /**
     * @return distance between x, y in meter using formula SA-B = arc cos (sin ϕA sin ϕB + cos ϕA cos ϕB cos dλ)
     */
    public int inMeter(){
        double distance = 0d;
        if ((!x.latitude.equals(y.latitude)) || (!x.longitude.equals(y.longitude))) {
            double deltaLon = Math.toRadians(y.getLongitude()) - Math.toRadians(x.getLongitude());
            distance = Math.acos(Math.sin(Math.toRadians(x.latitude))*Math.sin(Math.toRadians(y.latitude)) + Math.cos(Math.toRadians(x.latitude))*Math.cos(Math.toRadians(y.latitude))*Math.cos(deltaLon));
        }
        return (int) (distance * RADIUS);
    }
}

