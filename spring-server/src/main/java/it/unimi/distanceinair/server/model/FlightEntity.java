package it.unimi.distanceinair.server.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.UUID;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name="flight")
public class FlightEntity {
     @Id
     @GeneratedValue
     UUID id;
     String flightCode;
     String type;
     String iataCode;
     String icaoCode;
     String scheduledTime;
     String estimatedTime;
     String gate;
     String terminal;
     String actualRunway;
     String actualTime;
     String baggage;
     String delay;
     String estimatedRunway;

}
