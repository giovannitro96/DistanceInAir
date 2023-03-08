package it.unimi.distanceinair.server.service.soap;

import io.jsonwebtoken.Claims;
import it.unimi.distanceinair.server.service.endpointservices.FlightsService;
import it.unimi.distanceinair.server.util.TokenUtil;
import it.unimi.distanceinair.server.xml.domain.RemoveFlightForUserRequest;
import it.unimi.distanceinair.server.xml.domain.RemoveFlightForUserResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.context.MessageContext;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;
import org.springframework.ws.soap.SoapBody;
import org.springframework.ws.soap.SoapMessage;
import org.springframework.ws.soap.server.endpoint.annotation.SoapAction;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.Source;
import javax.xml.transform.dom.DOMSource;
import java.util.UUID;

@Endpoint
@Slf4j
public class RemoveFlightForUserEndpoint {
    private static final String NAMESPACE_URI = "http://www.unimi.it/distanceinair/";

    @Autowired
    FlightsService flightsService;

    @Autowired
    TokenUtil tokenUtil;

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "RemoveFlightForUserRequest")
    @SoapAction(NAMESPACE_URI + "removeFlightForUser")
    @ResponsePayload
    public RemoveFlightForUserResponse removeFlightForUser(MessageContext messageContext) throws Exception {//@RequestPayload GetDistanceByFlightCodeRequest request) throws Exception {

        log.info("New message arrived");
        SoapMessage message = (SoapMessage) messageContext.getRequest();

        SoapBody soapBody = message.getSoapBody();
        Source bodySource = soapBody.getPayloadSource();
        DOMSource bodyDomSource = (DOMSource) bodySource;

        JAXBContext context = JAXBContext.newInstance(RemoveFlightForUserRequest.class);
        Unmarshaller unmarshaller = context.createUnmarshaller();

        RemoveFlightForUserRequest request = (RemoveFlightForUserRequest) unmarshaller.unmarshal(bodyDomSource);
        log.debug("New request arrived: {}", request);
        Claims claims = tokenUtil.getAllClaimsFromToken(request.getToken());

        RemoveFlightForUserResponse response = new RemoveFlightForUserResponse();
        response.setSuccess(String.valueOf(flightsService.removeFlight(claims.get("preferred_username").toString(), UUID.fromString(request.getUuid()))));

        return response;
    }
}
