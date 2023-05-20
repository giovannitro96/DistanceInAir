package it.unimi.distanceinair.server.service.soap;

import it.unimi.distanceinair.server.service.endpointservices.GetDistanceFromApi;
import it.unimi.distanceinair.server.xml.domain.GetPossibleFlightsResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ws.context.MessageContext;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;
import org.springframework.ws.soap.server.endpoint.annotation.SoapAction;

@Endpoint
@Slf4j
public class GetPossibleFlightsEndpoint {

    private static final String NAMESPACE_URI = "http://www.unimi.it/distanceinair/";

    final GetDistanceFromApi getDistanceFromApi;

    public GetPossibleFlightsEndpoint(GetDistanceFromApi getDistanceFromApi) {
        this.getDistanceFromApi = getDistanceFromApi;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "GetPossibleFlightsRequest")
    @SoapAction(NAMESPACE_URI + "getPossibleFlights")
    @ResponsePayload
    public GetPossibleFlightsResponse getDistance(MessageContext messageContext) throws Exception {//@RequestPayload GetDistanceByFlightCodeRequest request) throws Exception {

        log.debug("New message arrived");
        return getDistanceFromApi.getPossibleFlightsResponse();
    }
}
