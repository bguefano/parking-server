package bguefano.parkings.domain;

import lombok.Builder;
import lombok.Data;

@Data @Builder
public class GeographicArea {

    private Coordinates lowerCorner;

    private Coordinates upperCorner;

    /**
     * Return true if a given position is include in the geographic area
     * @param position
     * @return  
     */
    public boolean contains(Coordinates position){
        return lowerCorner.longitude <= position.longitude && position.longitude <= upperCorner.longitude && lowerCorner.latitude <= position.latitude && position.latitude <= upperCorner.latitude;
    }
}
