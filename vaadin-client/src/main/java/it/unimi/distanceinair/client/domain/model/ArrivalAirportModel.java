package it.unimi.distanceinair.client.domain.model;

import it.unimi.distanceinair.client.domain.xml.Airport;
import it.unimi.distanceinair.client.domain.xml.Arrival;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public class ArrivalAirportModel {
    public UUID id;
    public String code;
    public Arrival arrival;
    public Airport airport;
}
