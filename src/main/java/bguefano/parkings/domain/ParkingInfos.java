package bguefano.parkings.domain;

import lombok.Builder;
import lombok.Data;
import lombok.With;

@Data @Builder
public class ParkingInfos {

    /**
     * Nom du parking
     */
    private String name;

    /**
     * Coordonnées du parking
     */
    private Coordinates position;

    /**
     * Nombre de place total
     */
    private int capacity;

    /**
     * Nombre de place disponible
     */
    private int available;
}
