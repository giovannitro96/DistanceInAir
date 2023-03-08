package it.unimi.distanceinair.server.model;

import it.unimi.distanceinair.server.xml.domain.Airport;
import lombok.Data;

import java.util.List;

@Data
public class AirportResponseToObject {
    private String success;
    private List<Airport> data;
}
