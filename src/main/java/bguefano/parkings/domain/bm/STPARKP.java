package bguefano.parkings.domain.bm;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;

@Data
public class STPARKP {

    @JacksonXmlProperty(localName = "NOM")
    String name;

    @JacksonXmlProperty(localName = "geometry")
    Geometry geometry;

    @JacksonXmlProperty(localName = "TOTAL")
    Integer total;

    @JacksonXmlProperty(localName = "LIBRES")
    Integer libres;
}
