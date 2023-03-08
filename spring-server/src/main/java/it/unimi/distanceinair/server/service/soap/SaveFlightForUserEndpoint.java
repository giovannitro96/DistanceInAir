package it.unimi.distanceinair.server.service.soap;

import io.jsonwebtoken.Claims;
import it.unimi.distanceinair.server.service.endpointservices.FlightsService;
import it.unimi.distanceinair.server.util.TokenUtil;
import it.unimi.distanceinair.server.xml.domain.SaveFlightForUserRequest;
import it.unimi.distanceinair.server.xml.domain.SaveFlightForUserResponse;
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

@Endpoint
@Slf4j
public class SaveFlightForUserEndpoint {
    private static final String NAMESPACE_URI = "http://www.unimi.it/distanceinair/";

    @Autowired
    FlightsService flightsService;

    @Autowired
    TokenUtil tokenUtil;

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "SaveFlightForUserRequest")
    @SoapAction(NAMESPACE_URI + "saveFlightForUser")
    @ResponsePayload
    public SaveFlightForUserResponse saveFlightForUser(MessageContext messageContext) throws Exception {//@RequestPayload GetDistanceByFlightCodeRequest request) throws Exception {

        log.info("New message arrived");
        SoapMessage message = (SoapMessage) messageContext.getRequest();

        SoapBody soapBody = message.getSoapBody();
        Source bodySource = soapBody.getPayloadSource();
        DOMSource bodyDomSource = (DOMSource) bodySource;

        JAXBContext context = JAXBContext.newInstance(SaveFlightForUserRequest.class);
        Unmarshaller unmarshaller = context.createUnmarshaller();

        SaveFlightForUserRequest request = (SaveFlightForUserRequest) unmarshaller.unmarshal(bodyDomSource);
        log.debug("New request arrived: {}", request);
        Claims claims = tokenUtil.getAllClaimsFromToken(request.getToken());

        SaveFlightForUserResponse response = new SaveFlightForUserResponse();
        response.setId(flightsService.saveFlight(claims.get("preferred_username").toString(), request.getFlight()).toString());

        return response;
    }
}
