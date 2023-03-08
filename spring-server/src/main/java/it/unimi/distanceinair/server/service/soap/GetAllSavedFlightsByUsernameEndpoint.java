package it.unimi.distanceinair.server.service.soap;

import com.google.gson.Gson;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.gson.io.GsonDeserializer;
import it.unimi.distanceinair.server.service.endpointservices.FlightsService;
import it.unimi.distanceinair.server.util.TokenUtil;
import it.unimi.distanceinair.server.xml.domain.GetAllSavedFlightsByUsernameRequest;
import it.unimi.distanceinair.server.xml.domain.GetAllSavedFlightsByUsernameResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
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
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Optional;

@Endpoint
@Slf4j
public class GetAllSavedFlightsByUsernameEndpoint {

    private static final String NAMESPACE_URI = "http://www.unimi.it/distanceinair/";

    @Autowired
    FlightsService flightsService;

    @Autowired
    TokenUtil tokenUtil;

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "GetAllSavedFlightsByUsernameRequest")
    @SoapAction(NAMESPACE_URI + "getAllFlights")
    @ResponsePayload
    public GetAllSavedFlightsByUsernameResponse getAllFlights(MessageContext messageContext) throws Exception {//@RequestPayload GetDistanceByFlightCodeRequest request) throws Exception {
        log.debug("New message arrived");
        SoapMessage message = (SoapMessage) messageContext.getRequest();

        SoapBody soapBody = message.getSoapBody();
        Source bodySource = soapBody.getPayloadSource();
        DOMSource bodyDomSource = (DOMSource) bodySource;

        JAXBContext context = JAXBContext.newInstance(GetAllSavedFlightsByUsernameRequest.class);
        Unmarshaller unmarshaller = context.createUnmarshaller();

        GetAllSavedFlightsByUsernameRequest request = (GetAllSavedFlightsByUsernameRequest) unmarshaller.unmarshal(bodyDomSource);
        log.debug("New request arrived: {}", request);

        Claims claims = tokenUtil.getAllClaimsFromToken(request.getToken());

        GetAllSavedFlightsByUsernameResponse response = new GetAllSavedFlightsByUsernameResponse();
        response.setData(flightsService.getAllFlights(claims.get("preferred_username").toString()));
        return response;
    }

    }
