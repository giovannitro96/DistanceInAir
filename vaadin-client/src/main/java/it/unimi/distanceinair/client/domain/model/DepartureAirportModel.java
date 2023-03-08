package it.unimi.distanceinair.client.domain.model;

import it.unimi.distanceinair.client.domain.xml.Airport;
import it.unimi.distanceinair.client.domain.xml.Departure;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public class DepartureAirportModel {
    public UUID id;
    public String code;
    public Departure departure;
    public Airport airport;
}
