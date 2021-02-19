package bguefano.parkings.domain;

import lombok.Data;

@Data(staticConstructor="of")
public class Coordinates {

    final Double longitude;

    final Double latitude;

    /**
     *
     * @param position a string in format "latitude longitude"
     * @return coordinates corresponding to the given string
     */
    public static Coordinates of(String position) {
        Coordinates coordinates = null;
        if (position.matches("^-?\\d*\\.{0,1}\\d+\\s-?\\d*\\.{0,1}\\d+$")){
            String[] coord = position.split(" ");
            coordinates = Coordinates.of(Double.valueOf(coord[1]), Double.valueOf(coord[0]));
        }
        return coordinates;
    }
}
