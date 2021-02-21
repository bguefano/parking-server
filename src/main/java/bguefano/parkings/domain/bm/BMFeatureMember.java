package bguefano.parkings.domain.bm;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;

@Data
public class BMFeatureMember {
    @JacksonXmlProperty(namespace = "bm", localName = "ST_PARK_P")
    STPARKP stparkp;
}
