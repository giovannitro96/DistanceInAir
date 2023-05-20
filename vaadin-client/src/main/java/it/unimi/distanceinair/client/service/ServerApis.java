package it.unimi.distanceinair.client.service;

import it.unimi.distanceinair.client.config.AppProperties;
import it.unimi.distanceinair.client.domain.model.ArrivalAirportModel;
import it.unimi.distanceinair.client.domain.model.DepartureAirportModel;
import it.unimi.distanceinair.client.domain.xml.*;
import org.springframework.ws.client.core.support.WebServiceGatewaySupport;
import org.springframework.ws.soap.client.core.SoapActionCallback;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ServerApis extends WebServiceGatewaySupport {

    AppProperties appProperties;

    public ServerApis(AppProperties appProperties) {
        this.appProperties = appProperties;
    }

    public ArrivalAirportModel getArrival(String code) throws IOException {

        GetDistanceByFlightCodeRequest request = new GetDistanceByFlightCodeRequest();
        request.setFlightCode(code);
        request.setType("arrival");

        GetDistanceByFlightCodeResponse response = (GetDistanceByFlightCodeResponse) getWebServiceTemplate()
                .marshalSendAndReceive(appProperties.getApiUrl() + "ws", request, new SoapActionCallback(
                        "http://www.unimi.it/distanceinair/getDistance"));

        Arrival arrival = response.getData().get(0).getArrival();
        Airport airport = response.getData().get(0).getAirport();
        String iataCode = response.getData().get(0).getFlight().getIataNumber();

        return new ArrivalAirportModel(null, iataCode, arrival, airport);
    }

    public DepartureAirportModel getDeparture(String code) {

        GetDistanceByFlightCodeRequest request = new GetDistanceByFlightCodeRequest();
        request.setFlightCode(code);
        request.setType("departure");

        GetDistanceByFlightCodeResponse response = (GetDistanceByFlightCodeResponse) getWebServiceTemplate()
                .marshalSendAndReceive(appProperties.getApiUrl() + "ws", request, new SoapActionCallback(
                        "http://www.unimi.it/distanceinair/getDistance"));

        Airport airport = response.getData().get(0).getAirport();
        Departure departure = response.getData().get(0).getDeparture();
        String iataCode = response.getData().get(0).getFlight().getIataNumber();

        return new DepartureAirportModel(null, iataCode, departure, airport);

    }

    public List<DistanceInAir> getPossibleFlights() {
        GetPossibleFlightsRequest request = new GetPossibleFlightsRequest();
        GetPossibleFlightsResponse response = (GetPossibleFlightsResponse) getWebServiceTemplate()
                .marshalSendAndReceive(appProperties.getApiUrl() + "ws", request, new SoapActionCallback(
                        "http://www.unimi.it/distanceinair/getPossibleFlights"));
        return response.getFlights();
    }

    public List<FlightDto> getAllSavedFlights(String token) {

        GetAllSavedFlightsByUsernameRequest request = new GetAllSavedFlightsByUsernameRequest();
        request.setToken(token);
        GetAllSavedFlightsByUsernameResponse response = (GetAllSavedFlightsByUsernameResponse) getWebServiceTemplate()
                .marshalSendAndReceive(appProperties.getApiUrl() + "ws", request, new SoapActionCallback(
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