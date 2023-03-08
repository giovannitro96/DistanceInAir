package it.unimi.distanceinair.client.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.unimi.distanceinair.client.config.AppProperties;
import it.unimi.distanceinair.client.domain.exception.WrongPasswordException;
import it.unimi.distanceinair.client.domain.model.ArrivalAirportModel;
import it.unimi.distanceinair.client.domain.model.DepartureAirportModel;
import it.unimi.distanceinair.client.domain.xml.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.client.core.support.WebServiceGatewaySupport;
import org.springframework.ws.soap.client.core.SoapActionCallback;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ServerApis extends WebServiceGatewaySupport {

    @Autowired
    AppProperties appProperties;

    private void writeToArrivalFile(Arrival arrival)  {

        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.writeValue(new FileWriter("/Users/giovannitroia/Università/Sicurezza delle architetture/SOASec Project/vaadin-client/src/main/resources/outputArrival.json"), arrival);
        } catch (IOException e) {
          e.printStackTrace();
      }
    }

    private void writeToAirportFile(Airport airport)  {

        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.writeValue(new FileWriter("/Users/giovannitroia/Università/Sicurezza delle architetture/SOASec Project/vaadin-client/src/main/resources/outputAirport.json"), airport);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void writeToDepartureFile(Departure departure)  {

        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.writeValue(new FileWriter("/Users/giovannitroia/Università/Sicurezza delle architetture/SOASec Project/vaadin-client/src/main/resources/outputDeparture.json"), departure);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private Arrival readFromArrivalFile() {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(new FileReader("/Users/giovannitroia/Università/Sicurezza delle architetture/SOASec Project/vaadin-client/src/main/resources/outputArrival.json"), Arrival.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private Airport readFromAirportFile() {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(new FileReader("/Users/giovannitroia/Università/Sicurezza delle architetture/SOASec Project/vaadin-client/src/main/resources/outputAirport.json"), Airport.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private Departure readFromDepartureFile() {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(new FileReader("/Users/giovannitroia/Università/Sicurezza delle architetture/SOASec Project/vaadin-client/src/main/resources/outputDeparture.json"), Departure.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public ArrivalAirportModel getArrival(String code) throws IOException, WrongPasswordException {
        if (appProperties.isSoapActive()) {

            GetDistanceByFlightCodeRequest request = new GetDistanceByFlightCodeRequest();
            request.setFlightCode(code);
            request.setType("arrival");

            GetDistanceByFlightCodeResponse response = (GetDistanceByFlightCodeResponse) getWebServiceTemplate()
                    .marshalSendAndReceive(appProperties.getApiUrl()+"ws", request, new SoapActionCallback(
                            "http://www.unimi.it/distanceinair/getDistance"));

            Arrival arrival = response.getData().get(0).getArrival();
            Airport airport = response.getData().get(0).getAirport();
            String iataCode = response.getData().get(0).getFlight().getIataNumber();
            writeToArrivalFile(arrival);
            writeToAirportFile(airport);

            return new ArrivalAirportModel(null, iataCode, arrival, airport);
        } else {
            return new ArrivalAirportModel(null, "az555", readFromArrivalFile(), readFromAirportFile());
        }
    }
    public DepartureAirportModel getDeparture(String code) throws WrongPasswordException {
        if (appProperties.isSoapActive()) {

            GetDistanceByFlightCodeRequest request = new GetDistanceByFlightCodeRequest();
            request.setFlightCode(code);
            request.setType("departure");

            GetDistanceByFlightCodeResponse response = (GetDistanceByFlightCodeResponse) getWebServiceTemplate()
                    .marshalSendAndReceive(appProperties.getApiUrl()+"ws", request, new SoapActionCallback(
                            "http://www.unimi.it/distanceinair/getDistance"));

            Airport airport = response.getData().get(0).getAirport();
            Departure departure = response.getData().get(0).getDeparture();
            String iataCode = response.getData().get(0).getFlight().getIataNumber();

            writeToDepartureFile(departure);
            writeToAirportFile(airport);

            return new DepartureAirportModel(null, iataCode, departure, airport);
        } else {
            return new DepartureAirportModel(null, "ag555", readFromDepartureFile(), readFromAirportFile());
        }
    }

    public List<FlightDto> getAllSavedFlights(String token) {

        GetAllSavedFlightsByUsernameRequest request = new GetAllSavedFlightsByUsernameRequest();
        request.setToken(token);
        GetAllSavedFlightsByUsernameResponse response = (GetAllSavedFlightsByUsernameResponse) getWebServiceTemplate()
                .marshalSendAndReceive(appProperties.getApiUrl()+"ws", request, new SoapActionCallback(
                        "http://www.unimi.it/distanceinair/getAllFlights"));

        return new ArrayList<>(response.getData());
    }

    public String saveFlightIntoPref(DistanceInAir flight, String token) {
        SaveFlightForUserRequest request = new SaveFlightForUserRequest();
        request.setToken(token);
        request.setFlight(flight);
        SaveFlightForUserResponse response = (SaveFlightForUserResponse) getWebServiceTemplate()
                .marshalSendAndReceive(appProperties.getApiUrl()+"ws", request, new SoapActionCallback(
                        "http://www.unimi.it/distanceinair/saveFlightForUser"));
        return response.getId();
    }

    //cinfigurare il remove

    public Boolean removeFlightFromPref(String token, UUID flightCode) {
        RemoveFlightForUserRequest request = new RemoveFlightForUserRequest();
        request.setToken(token);
        request.setUuid(flightCode.toString());
        RemoveFlightForUserResponse response = (RemoveFlightForUserResponse) getWebServiceTemplate()
                .marshalSendAndReceive(appProperties.getApiUrl()+"ws", request, new SoapActionCallback(
                        "http://www.unimi.it/distanceinair/removeFlightForUser"));
        return Boolean.parseBoolean(response.getSuccess());
    }

}