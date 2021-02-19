package bguefano.parkings.provider;

import bguefano.parkings.domain.GeographicArea;
import bguefano.parkings.domain.ParkingInfos;

import java.util.List;
import java.util.function.Predicate;

public interface ParkingLocator {

    /**
     * Find all {@link ParkingInfos} matching the given criteria
     * @param criteria
     * @return
     */
    List<ParkingInfos> find(Predicate<ParkingInfos> criteria);

    /**
     *
     * @return the geographic area where the provider operate
     */
    GeographicArea getGeographicArea();
}
