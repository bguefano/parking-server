package bguefano.parkings.provider;

import bguefano.parkings.domain.ParkingInfos;
import bguefano.parkings.domain.gml.FeatureCollection;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;


@Slf4j
public class BaseParkingLocator<T> implements ParkingLocator {

    private final RestTemplate restTemplate;

    private final ParkingInfos.Deserializer<T> deserializer;

    private final ParameterizedTypeReference<FeatureCollection<T>> typeReference;

    @Value("${url}")
    private String url;


    public BaseParkingLocator(RestTemplate restTemplate, ParkingInfos.Deserializer<T> deserializer, ParameterizedTypeReference<FeatureCollection<T>> typeReference) {
        this.restTemplate = restTemplate;
        this.deserializer = deserializer;
        this.typeReference = typeReference;
    }

    @Override
    public List<ParkingInfos> findAll() {
        List<ParkingInfos> parkings = new ArrayList<>();

        FeatureCollection<T> featureCollection  = null;
        try {
            RequestEntity<?> request = RequestEntity
                    .get(url)
                    .accept(MediaType.TEXT_XML)
                    .build();
            featureCollection = restTemplate.exchange(request, typeReference).getBody();
        } catch (Exception ex){
            log.error("External service error", ex);
        }
        if (featureCollection != null){
            for (T member: featureCollection.getFeatureMembers()) {
               parkings.add(deserializer.toParkingInfos(member));
            }
        }
        log.info("Found {} parking(s)", parkings.size());
        return parkings;
    }
}
