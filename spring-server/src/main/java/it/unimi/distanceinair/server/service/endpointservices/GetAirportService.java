package it.unimi.distanceinair.server.service.endpointservices;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.unimi.distanceinair.server.xml.domain.Airport;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@Service
@Slf4j
public class GetAirportService {

    final String AIRPORT_BASE_URL = "https://airport-info.p.rapidapi.com/airport";

    public Airport getAirportByIataCode(String iataCode) {
        try {
            log.debug("Calling url for airport data with iataCode: {}", iataCode);
            RestTemplate restTemplate = new RestTemplate();
            URI targetUrl = UriComponentsBuilder.fromUriString(AIRPORT_BASE_URL)
                    .queryParam("iata", iataCode)
                    .build()
                    .encode()
                    .toUri();
            HttpHeaders headers = new HttpHeaders();
            headers.set("X-RapidAPI-Key", "88d9a9dda1mshc4fc8b48c53a1b7p1b37dajsn34e012408642");
            headers.set("X-RapidAPI-Host", "airport-info.p.rapidapi.com");
            HttpEntity<String> entity = new HttpEntity<>("", headers);

            ResponseEntity<String> airport = restTemplate.exchange(targetUrl, HttpMethod.GET, entity, String.class);
            ObjectMapper mapper = new ObjectMapper();
            mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
            return mapper.readValue(airport.getBody(), Airport.class);
        } catch (JsonProcessingException e) {
            log.error("Errore di mapping della risposta.");
            return new Airport();
        }
    }
}
