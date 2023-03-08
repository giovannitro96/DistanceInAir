package it.unimi.distanceinair.server.service.endpointservices;

import it.unimi.distanceinair.server.converter.BasicConverter;
import it.unimi.distanceinair.server.dao.FlightsDao;
import it.unimi.distanceinair.server.model.FlightEntity;
import it.unimi.distanceinair.server.xml.domain.DistanceInAir;
import it.unimi.distanceinair.server.xml.domain.FlightDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class FlightsService {

    @Autowired
    FlightsDao flightsDao;

    @Autowired
    GetAirportService getAirportService;

    @Autowired
    BasicConverter conv;
    public List<FlightDto> getAllFlights(String username) {
        log.info("GET user {} requested all his flights", username);
        return flightsDao.getAllFlights(username).stream()
                .map(it -> conv.map(it, FlightDto.class)).toList().stream()
               .peek(it ->
                       it.setAirport(getAirportService.getAirportByIataCode(it.getIataCode())))
               .collect(Collectors.toList());
    }

    public UUID saveFlight(String username, DistanceInAir flight) {
        log.info("POST user {} saved flight with code {}", username, flight.getFlight().getIataNumber());
        FlightDto dto;
        if(flight.getArrival() != null) {
            dto = conv.map(flight.getArrival(), FlightDto.class);
            dto.setType("arrival");
            dto.setFlightCode(flight.getFlight().getIataNumber());
        } else {
            dto = conv.map(flight.getDeparture(), FlightDto.class);
            dto.setType("departure");
            dto.setFlightCode(flight.getFlight().getIataNumber());
        }

        return flightsDao.saveFlight(username, conv.map(dto, FlightEntity.class));
    }

    public boolean removeFlight(String username, UUID flightCode) {
        log.info("DELETE user {} removed flight with code {}", username, flightCode);
       return flightsDao.removeFlight(username, flightCode);
    }
}
