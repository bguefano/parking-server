package bguefano.parkings.domain.bm;

import bguefano.parkings.domain.gml.Point;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;

@Data
public class Geometry {
    @JacksonXmlProperty(namespace = "gml", localName = "Point")
    Point point;
}
