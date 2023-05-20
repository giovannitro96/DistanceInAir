package it.unimi.distanceinair.server.dao;

import it.unimi.distanceinair.server.model.FlightEntity;
import it.unimi.distanceinair.server.model.UserEntity;
import it.unimi.distanceinair.server.repository.FlightRepository;
import it.unimi.distanceinair.server.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
@Slf4j
public class FlightsDao {

    final UserRepository userRepository;

    final FlightRepository flightRepository;

    public FlightsDao(UserRepository userRepository,
                      FlightRepository flightRepository) {
        this.userRepository = userRepository;
        this.flightRepository = flightRepository;
    }

    @Transactional
    public List<FlightEntity> getAllFlights(String username) {
        UserEntity userEntity = userRepository.findUserEntityByUsername(username);
        if (userEntity == null) {
            UserEntity newUser = new UserEntity();
            newUser.setUsername(username);
            newUser.setFlightsList(new ArrayList<>());
            userRepository.save(newUser);
            return new ArrayList<>();
        }
        return userEntity.getFlightsList();
    }

    public UUID saveFlight(String username, FlightEntity flightEntity) {
        try {
            FlightEntity flight = flightRepository.findFlightEntityByFlightCodeAndScheduledTime(flightEntity.getFlightCode(), flightEntity.getScheduledTime());

                UUID id = UUID.randomUUID();
                flightEntity.setId(id);
                UserEntity userEntity = userRepository.findUserEntityByUsername(username);
                if (userEntity == null) {
                    UserEntity newUser = new UserEntity();
                    newUser.setUsername(username);
                    newUser.setFlightsList(new ArrayList<>());
                    userEntity = newUser;
                }
                if(flight == null || !flightsEqual(flight, flightEntity)) {
                    FlightEntity savedEntity = flightRepository.save(flightEntity);
                    userEntity.getFlightsList().add(savedEntity);
                    userRepository.save(userEntity);
                } else if(!userEntity.getFlightsList().contains(flight)){
                    userEntity.getFlightsList().add(flight);
                    userRepository.save(userEntity);
                }
            return flightEntity.getId();
        } catch (Exception e) {
            log.error(e.getMessage());
            return null;
        }
    }

    public boolean flightsEqual(FlightEntity e1, FlightEntity e2) {
        return e1.getFlightCode().equals(e2.getFlightCode()) && e1.getScheduledTime().equals(e2.getScheduledTime());
    }

    public boolean removeFlight(String username, UUID flightCode) {
        try {
            FlightEntity flight = flightRepository.findById(flightCode).orElseThrow();
            UserEntity userEntity = userRepository.findUserEntityByUsername(username);
            userEntity.getFlightsList().remove(flight);
            userRepository.save(userEntity);
            return true;
        } catch (Exception e) {
            log.error(e.getMessage());
            return false;
        }
    }
}
