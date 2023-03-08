package it.unimi.distanceinair.server.service.soap;

import it.unimi.distanceinair.server.service.endpointservices.GetDistanceFromApi;
import it.unimi.distanceinair.server.xml.domain.GetDistanceByFlightCodeRequest;
import it.unimi.distanceinair.server.xml.domain.GetDistanceByFlightCodeResponse;
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
public class GetDistanceInAirEndpoint {

    private static final String NAMESPACE_URI = "http://www.unimi.it/distanceinair/";

    @Autowired
    GetDistanceFromApi getDistanceFromApi;

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "GetDistanceByFlightCodeRequest")
    @SoapAction(NAMESPACE_URI + "getDistance")
    @ResponsePayload
    public GetDistanceByFlightCodeResponse getDistance(MessageContext messageContext) throws Exception {//@RequestPayload GetDistanceByFlightCodeRequest request) throws Exception {

        log.debug("New message arrived");
        SoapMessage message = (SoapMessage) messageContext.getRequest();

        SoapBody soapBody = message.getSoapBody();
        Source bodySource = soapBody.getPayloadSource();
        DOMSource bodyDomSource = (DOMSource) bodySource;

        JAXBContext context = JAXBContext.newInstance(GetDistanceByFlightCodeRequest.class);
        Unmarshaller unmarshaller = context.createUnmarshaller();

        GetDistanceByFlightCodeRequest request = (GetDistanceByFlightCodeRequest) unmarshaller.unmarshal(bodyDomSource);
        log.info("New request arrived: {}", request);
        return getDistanceFromApi.getFromApi(request);
    }

}
