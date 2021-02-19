package bguefano.parkings;

import bguefano.parkings.domain.Coordinates;
import bguefano.parkings.domain.Distance;
import bguefano.parkings.domain.ParkingInfos;
import bguefano.parkings.provider.ParkingLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ParkingService {

    public static Logger logger  = LoggerFactory.getLogger(ParkingService.class);

    @Autowired
    List<ParkingLocator> locators;

    /**
     *
     * @param position
     * @param distanceMax
     * @return
     */
    List<ParkingInfos> locate(Coordinates position, double distanceMax) {
        List<ParkingInfos> parkings = new ArrayList<>();
        for(ParkingLocator locator: locators) {
            if (locator.getGeographicArea().contains(position)) {
                logger.info("Position {} is in the area operated by car park locator {}", position, locator.getClass().getSimpleName());
                parkings.addAll(locator.find(parking -> Distance.between(parking.getPosition(), position).inMeter() <= distanceMax));
            }
        }
        return parkings;
    }
}
