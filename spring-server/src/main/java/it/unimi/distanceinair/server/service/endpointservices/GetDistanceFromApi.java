package it.unimi.distanceinair.server.service.endpointservices;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import it.unimi.distanceinair.server.config.AppProperties;
import it.unimi.distanceinair.server.xml.domain.GetDistanceByFlightCodeRequest;
import it.unimi.distanceinair.server.xml.domain.GetDistanceByFlightCodeResponse;
import it.unimi.distanceinair.server.xml.domain.GetPossibleFlightsResponse;
import it.unimi.distanceinair.server.xml.domain.exception.FlightNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;


@Service
@Slf4j
public class GetDistanceFromApi {

    final String BASE_URL = "https://app.goflightlabs.com/advanced-flights-schedules";
    final String AIRPORT_BASE_URL = "https://airport-info.p.rapidapi.com/airport";
    final AppProperties appProperties;

    final GetAirportService getAirportService;

    public GetDistanceFromApi(AppProperties appProperties,
                              GetAirportService getAirportService) {
        this.appProperties = appProperties;
        this.getAirportService = getAirportService;
    }

    public GetDistanceByFlightCodeResponse getFromApi(GetDistanceByFlightCodeRequest getDistanceByFlightCode) throws JsonProcessingException, FlightNotFoundException {
        log.info("Calling url for retrieving flight with code: {}, of type :{}", getDistanceByFlightCode.getFlightCode(), getDistanceByFlightCode.getType());
        RestTemplate restTemplate = new RestTemplate();
        URI targetUrl = UriComponentsBuilder.fromUriString(BASE_URL)  // Build the base link
                .queryParam("access_key", appProperties.getAccessToken())
                .queryParam("flight_iata", getDistanceByFlightCode.getFlightCode())// Add one or more query params
                .build()                                                 // Build the URL
                .encode()                                                // Encode any URI items that need to be encoded
                .toUri();

        ResponseEntity<String> result = restTemplate.getForEntity(targetUrl, String.class);
        try {
            GetDistanceByFlightCodeResponse response = new ObjectMapper().readValue(result.getBody(), GetDistanceByFlightCodeResponse.class);
            String iataCode = getDistanceByFlightCode.getType().equals("arrival") ? response.getData().get(0).getArrival().getIataCode() : response.getData().get(0).getDeparture().getIataCode();
            response.getData().get(0).setAirport(getAirportService.getAirportByIataCode(iataCode));
            log.info("Response returned from api: {}", response.getData());
            return response;
        } catch (
                MismatchedInputException e) {
            throw new FlightNotFoundException("Flight code \"" + getDistanceByFlightCode.getFlightCode() + "\" not found or not correct.");
        }
    }

    public GetPossibleFlightsResponse getPossibleFlightsResponse() {
        log.info("Calling url for retrieving possible flights");
        RestTemplate restTemplate = new RestTemplate();
        URI targetUrl = UriComponentsBuilder.fromUriString(BASE_URL)
                .queryParam("access_key", appProperties.getAccessToken())
                .queryParam("limit", "10")
                .build()
                .encode()
                .toUri();

        ResponseEntity<String> result = restTemplate.getForEntity(targetUrl, String.class);
        try {
            GetDistanceByFlightCodeResponse response = new ObjectMapper().readValue(result.getBody(), GetDistanceByFlightCodeResponse.class);

            GetPossibleFlightsResponse response1 = new GetPossibleFlightsResponse();
            response.getData().subList(10, response.getData().size()).clear();
            response1.setFlights(response.getData());
            response1.getFlights().forEach(it -> {
                it.setAirport(it.getType().equals("arrival") ? getAirportService.getAirportByIataCode(it.getArrival().getIataCode()) : getAirportService.getAirportByIataCode(it.getDeparture().getIataCode()));
            });
            return response1;
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

}
