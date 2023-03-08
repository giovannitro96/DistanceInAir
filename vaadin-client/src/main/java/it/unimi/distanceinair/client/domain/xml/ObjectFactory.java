//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.7 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2023.03.01 at 03:02:03 PM CET 
//


package it.unimi.distanceinair.client.domain.xml;


import javax.xml.bind.annotation.XmlRegistry;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the it.unimi.distanceinair package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {


    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: it.unimi.distanceinair
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link GetDistanceByFlightCodeRequest }
     * 
     */
    public GetDistanceByFlightCodeRequest createGetDistanceByFlightCodeRequest() {
        return new GetDistanceByFlightCodeRequest();
    }

    /**
     * Create an instance of {@link RemoveFlightForUserRequest }
     * 
     */
    public RemoveFlightForUserRequest createRemoveFlightForUserRequest() {
        return new RemoveFlightForUserRequest();
    }

    /**
     * Create an instance of {@link RemoveFlightForUserResponse }
     * 
     */
    public RemoveFlightForUserResponse createRemoveFlightForUserResponse() {
        return new RemoveFlightForUserResponse();
    }

    /**
     * Create an instance of {@link GetAllSavedFlightsByUsernameRequest }
     * 
     */
    public GetAllSavedFlightsByUsernameRequest createGetAllSavedFlightsByUsernameRequest() {
        return new GetAllSavedFlightsByUsernameRequest();
    }

    /**
     * Create an instance of {@link SaveFlightForUserRequest }
     * 
     */
    public SaveFlightForUserRequest createSaveFlightForUserRequest() {
        return new SaveFlightForUserRequest();
    }

    /**
     * Create an instance of {@link DistanceInAir }
     * 
     */
    public DistanceInAir createDistanceInAir() {
        return new DistanceInAir();
    }

    /**
     * Create an instance of {@link SaveFlightForUserResponse }
     * 
     */
    public SaveFlightForUserResponse createSaveFlightForUserResponse() {
        return new SaveFlightForUserResponse();
    }

    /**
     * Create an instance of {@link GetDistanceByFlightCodeResponse }
     * 
     */
    public GetDistanceByFlightCodeResponse createGetDistanceByFlightCodeResponse() {
        return new GetDistanceByFlightCodeResponse();
    }

    /**
     * Create an instance of {@link GetAllSavedFlightsByUsernameResponse }
     * 
     */
    public GetAllSavedFlightsByUsernameResponse createGetAllSavedFlightsByUsernameResponse() {
        return new GetAllSavedFlightsByUsernameResponse();
    }

    /**
     * Create an instance of {@link FlightDto }
     * 
     */
    public FlightDto createFlightDto() {
        return new FlightDto();
    }

    /**
     * Create an instance of {@link Airline }
     * 
     */
    public Airline createAirline() {
        return new Airline();
    }

    /**
     * Create an instance of {@link Departure }
     * 
     */
    public Departure createDeparture() {
        return new Departure();
    }

    /**
     * Create an instance of {@link Flight }
     * 
     */
    public Flight createFlight() {
        return new Flight();
    }

    /**
     * Create an instance of {@link Airport }
     * 
     */
    public Airport createAirport() {
        return new Airport();
    }

    /**
     * Create an instance of {@link Arrival }
     * 
     */
    public Arrival createArrival() {
        return new Arrival();
    }

}