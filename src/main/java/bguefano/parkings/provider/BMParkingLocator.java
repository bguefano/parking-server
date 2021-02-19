package bguefano.parkings.provider;

import bguefano.parkings.ParkingController;
import bguefano.parkings.domain.GeographicArea;
import bguefano.parkings.domain.Coordinates;
import bguefano.parkings.domain.ParkingInfos;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpRequest;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

/**
 * Bordeaux Metropole parking locator
 */
@Component
public class BMParkingLocator implements ParkingLocator {

    public static Logger logger  = LoggerFactory.getLogger(ParkingController.class);

    private static final String URL = "http://data.lacub.fr/wfs?key={apiKey}&SERVICE=WFS&VERSION=1.1.0&REQUEST=GetFeature&TYPENAME=ST_PARK_P&SRSNAME=EPSG:4326";

    private final RestTemplate restTemplate;

    private GeographicArea area;

    @Value("${bm.apiKey}")
    private String apikey;


    public BMParkingLocator(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.interceptors(Arrays.asList(new RestTemplateMimeModifierInterceptor())).build();
    }

    @Override
    public List<ParkingInfos> find(Predicate<ParkingInfos> criteria) {
        List<ParkingInfos> parkings = new ArrayList<>();
        try {
            FeatureCollection featureCollection = restTemplate.getForObject(URL, FeatureCollection.class, apikey);
            for (FeatureMember member: featureCollection.getFeatureMembers()) {
                ParkingInfos parking = ParkingInfos.builder()
                        .name(member.st_park_p.getName())
                        .position(Coordinates.of(member.getSt_park_p().getGeometry().getPoint().getPos()))
                        .capacity(member.st_park_p.getTotal())
                        .available(member.st_park_p.getLibres())
                        .build();
                if (criteria.test(parking)){
                    parkings.add(parking);
                }
            }
        } catch (Exception ex){
            logger.error("External service error", ex);
        }
        logger.info("Found {} car park matching the criteria", parkings.size());
        return parkings;
    }

    @Override @PostConstruct
    public GeographicArea getGeographicArea() {
        if (this.area == null){
            FeatureCollection featureCollection = restTemplate.getForObject(URL, FeatureCollection.class, apikey);
            this.area = GeographicArea.builder()
                    .lowerCorner(Coordinates.of(featureCollection.getBoundedBy().getEnvelope().getLowerCorner()))
                    .upperCorner(Coordinates.of(featureCollection.getBoundedBy().getEnvelope().getUpperCorner()))
                    .build();
        }
        return area;
    }

    /**
     * Workaround to fix mime type parsing errors
     */
    class RestTemplateMimeModifierInterceptor implements ClientHttpRequestInterceptor {

        @Override
        public ClientHttpResponse intercept(
                HttpRequest request,
                byte[] body,
                ClientHttpRequestExecution execution) throws IOException {

            ClientHttpResponse response = execution.execute(request, body);
            response.getHeaders().setContentType(new MediaType(MediaType.TEXT_XML, StandardCharsets.ISO_8859_1));
            return response;
        }

    }

    // JAXB POJO

    @XmlRootElement(namespace = "http://www.opengis.net/wfs", name = "FeatureCollection")
    @XmlAccessorType(XmlAccessType.FIELD)
    @Data
    static class FeatureCollection {

        @XmlElement(namespace = "http://www.opengis.net/gml", name = "featureMember")
        List<FeatureMember> featureMembers;

        @XmlElement(namespace = "http://www.opengis.net/gml")
        BoundedBy boundedBy;
    }

    // TODO use generic classe and move to the util package
    @XmlRootElement(namespace = "http://www.opengis.net/gml", name = "featureMember")
    @XmlAccessorType(XmlAccessType.FIELD)
    @Data
    static class FeatureMember {

        @XmlElement(namespace = "http://data.bordeaux-metropole.fr/wfs", name = "ST_PARK_P")
        ST_PARK_P st_park_p;
    }

    @XmlRootElement(namespace = "http://www.opengis.net/gml", name = "Point")
    @XmlAccessorType(XmlAccessType.FIELD)
    @Data
    static class Point {

        @XmlElement(namespace = "http://www.opengis.net/gml", name = "pos")
        String pos;


    }

    @XmlRootElement(namespace = "http://www.opengis.net/gml", name = "boundedBy")
    @XmlAccessorType(XmlAccessType.FIELD)
    @Data
    static class BoundedBy {

        @XmlElement(namespace = "http://www.opengis.net/gml", name = "Envelope")
        Envelope envelope;


    }

    @XmlRootElement(namespace = "http://www.opengis.net/gml", name = "Envelope")
    @XmlAccessorType(XmlAccessType.FIELD)
    @Data
    static class Envelope {

        @XmlElement(namespace = "http://www.opengis.net/gml", name = "lowerCorner")
        String lowerCorner;

        @XmlElement(namespace = "http://www.opengis.net/gml", name = "upperCorner")
        String upperCorner;

    }

    @XmlRootElement(namespace = "http://data.bordeaux-metropole.fr/wfs", name = "ST_PARK_P")
    @XmlAccessorType(XmlAccessType.FIELD)
    @Data
    static class  ST_PARK_P {

        @XmlElement(namespace = "http://data.bordeaux-metropole.fr/wfs", name = "NOM")
        String name;

        @XmlElement(namespace = "http://data.bordeaux-metropole.fr/wfs", name = "geometry")
        Geometry geometry;

        @XmlElement(namespace = "http://data.bordeaux-metropole.fr/wfs", name = "TOTAL", defaultValue = "0")
        Integer total;

        @XmlElement(namespace = "http://data.bordeaux-metropole.fr/wfs", name = "LIBRES", defaultValue = "0")
        Integer libres;
    }

    @XmlRootElement(namespace = "http://data.bordeaux-metropole.fr/wfs", name = "geometry")
    @XmlAccessorType(XmlAccessType.FIELD)
    @Data
    static class  Geometry {

        @XmlElement(namespace = "http://www.opengis.net/gml", name = "Point")
        Point point;
    }
}
