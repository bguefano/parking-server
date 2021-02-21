package bguefano.parkings.domain.gml;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.Data;

// JAXB POJO
@Data
@JacksonXmlRootElement(namespace = "wfs", localName = "FeatureCollection")
public class FeatureCollection<T> {

    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(namespace = "gml", localName = "featureMember")
    T[] featureMembers;
}
