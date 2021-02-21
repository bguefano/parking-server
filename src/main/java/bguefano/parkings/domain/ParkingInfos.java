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
     * Distance to the parking
     */
    @With
    private Integer distance;

    /**
     * Nombre de place total
     */
    private Integer capacity;

    /**
     * Nombre de place disponible
     */
    private Integer available;

    public interface Deserializer<T> {
        ParkingInfos toParkingInfos(T member);
    }
}
