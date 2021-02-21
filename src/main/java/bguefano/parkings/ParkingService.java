package bguefano.parkings;

import bguefano.parkings.domain.Coordinates;
import bguefano.parkings.domain.Distance;
import bguefano.parkings.domain.ParkingInfos;
import bguefano.parkings.provider.ParkingLocator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service @Slf4j
public class ParkingService {

    ParkingLocator locator;

    @Autowired
    public ParkingService(ParkingLocator locator){
        this.locator = locator;
    }

    /**
     *
     * @param position
     * @param distanceMax
     * @return
     */
    List<ParkingInfos> locate(Coordinates position, Integer distanceMax) {
        List<ParkingInfos> parkings = new ArrayList<>();
            log.info("Position {} is in the area operated by car park locator {}", position, locator.getClass().getSimpleName());
            parkings.addAll(locator.findAll().stream()
                    .map(parking -> parking.withDistance(Distance.between(parking.getPosition(), position).inMeter()))
                    .filter(parking -> parking.getDistance() <= distanceMax)
                    .collect(Collectors.toList()));
        return parkings;
    }
}
