package bguefano.parkings.provider;

import bguefano.parkings.domain.bm.BMFeatureMember;
import bguefano.parkings.domain.gml.FeatureCollection;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpRequest;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;

@Configuration @ConditionalOnProperty(value="format", havingValue = "bm")
public class BMConfiguration {


    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder.additionalInterceptors(new RestTemplateMimeModifierInterceptor()).build();
    }

    @Bean
    ParkingLocator parkingLocator(RestTemplate resTemplate){
        return new BaseParkingLocator<BMFeatureMember>(resTemplate, new BMDeserializer(), new BMParameterizedTypeReference<>());
    }


    /**
     * Workaround to fix mime type parsing errors
     */
    private static class RestTemplateMimeModifierInterceptor implements ClientHttpRequestInterceptor {

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

    private static class BMParameterizedTypeReference<T> extends ParameterizedTypeReference<T> {

        @Override
        public Type getType() {
            return (new ParameterizedTypeReference<FeatureCollection<BMFeatureMember>>() {}).getType();
        }
    }
}
