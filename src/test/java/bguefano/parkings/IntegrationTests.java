package bguefano.parkings;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import bguefano.parkings.domain.ParkingInfos;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class IntegrationTests {

	@Autowired
	private TestRestTemplate restTemplate;


	@Test
	void should_return_car_park(){
		//Given
		double latitude = 44.855743;
		double longitude = -0.595144;

		//When
		ResponseEntity<ParkingInfos[]> parkings = restTemplate.getForEntity("/api/parkings?latitude={latitude}&longitude={longitude}", ParkingInfos[].class, latitude,longitude);

		//Then
		assertTrue(parkings.getBody().length != 0);

		for(ParkingInfos parkingInfos : parkings.getBody()){
			assertTrue(parkingInfos.getDistance() <= 1000);
		};
	}

	@Test
	void should_not_return_parkings() {
		//Given
		double latitude = 44.78657;
		double longitude = -0.58799;

		//When
		ResponseEntity<ParkingInfos[]> parkings = restTemplate.getForEntity("/api/parkings?latitude={latitude}&longitude={longitude}", ParkingInfos[].class, latitude,longitude);

		//Then
		assertFalse(parkings.getBody().length != 0);
	}

}
