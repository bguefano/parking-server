package bguefano.parkings.provider;

import bguefano.parkings.domain.ParkingInfos;

import java.util.List;

public interface ParkingLocator {

    /**
     * Find all {@link ParkingInfos}
     * @return
     */
    List<ParkingInfos> findAll();
}
