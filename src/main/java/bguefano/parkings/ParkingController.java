package bguefano.parkings;


import bguefano.parkings.domain.Coordinates;
import bguefano.parkings.domain.ParkingInfos;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api")
@Slf4j
public class ParkingController {

    @Autowired
    private ParkingService geolocationService;



    /**
     * Geolocate car park near the given position (latitude, longitude)
     *
     * @param longitude
     * @param latitude
     * @param distanceMax distance max in meter between the car park and the given position
     * @return
     */
    @GetMapping(value = "/parkings", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<List<ParkingInfos>> locate(@RequestParam Double longitude, @RequestParam Double latitude, @RequestParam(defaultValue = "1000") Integer distanceMax){
        log.info("GET /api/parkings?latitude={}&longitude={}&distanceMax={}", latitude, longitude, distanceMax);
        List<ParkingInfos> carParks = geolocationService.locate(Coordinates.of(longitude, latitude), distanceMax);
        return new ResponseEntity<>(carParks, HttpStatus.OK);
    }
}
