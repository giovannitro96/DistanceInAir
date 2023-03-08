package it.unimi.distanceinair.server.xml.domain.exception;

import org.springframework.ws.soap.server.endpoint.annotation.FaultCode;
import org.springframework.ws.soap.server.endpoint.annotation.SoapFault;

@SoapFault(faultCode = FaultCode.CUSTOM,
        customFaultCode = "{" + FlightNotFoundException.NAMESPACE_URI + "}custom_fault")
public class FlightNotFoundException extends Exception {

    private static final long serialVersionUID = 1L;
    public static final String NAMESPACE_URI = "http://www.unimi.it/distanceinair/";

    public FlightNotFoundException(String message) {
        super(message);
    }
}