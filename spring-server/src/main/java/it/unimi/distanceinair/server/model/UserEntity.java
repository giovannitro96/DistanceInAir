package it.unimi.distanceinair.server.model;

import it.unimi.distanceinair.server.xml.domain.DistanceInAir;
import it.unimi.distanceinair.server.xml.domain.Flight;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;
import java.util.UUID;


@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name="keyclockuser")
public class UserEntity {
    @Id
    @GeneratedValue()
    private UUID id;
    private String username;
    @OneToMany(fetch = FetchType.EAGER)
    private List<FlightEntity> flightsList;

}
