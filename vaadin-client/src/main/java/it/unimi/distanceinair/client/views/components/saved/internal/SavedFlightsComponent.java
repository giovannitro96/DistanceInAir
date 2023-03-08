package it.unimi.distanceinair.client.views.components.saved.internal;

import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import it.unimi.distanceinair.client.domain.model.ArrivalAirportModel;
import it.unimi.distanceinair.client.domain.model.DepartureAirportModel;
import it.unimi.distanceinair.client.domain.xml.Arrival;
import it.unimi.distanceinair.client.domain.xml.Departure;
import it.unimi.distanceinair.client.domain.xml.FlightDto;
import it.unimi.distanceinair.client.service.ServerApis;
import it.unimi.distanceinair.client.views.components.singleviews.ArrivalView;
import it.unimi.distanceinair.client.views.components.singleviews.DepartureView;

import java.util.List;
import java.util.UUID;

public class SavedFlightsComponent extends VerticalLayout {
    private final HorizontalLayout flexLayout;

    public SavedFlightsComponent(List<FlightDto> flightsList, ServerApis serverApis){

        flexLayout = new HorizontalLayout();
        flexLayout.getStyle().set("flex-wrap", "wrap");
        flexLayout.getStyle().set("display", "flex");
        flexLayout.setSpacing(true);
        flexLayout.setJustifyContentMode(JustifyContentMode.CENTER);
        setDefaultHorizontalComponentAlignment(Alignment.CENTER);

        flightsList.forEach(it -> {
            if (it.getType().equals("arrival")) {
                Arrival arrival = new Arrival();
                arrival.setBaggage(it.getBaggage());
                arrival.setActualRunway(it.getActualRunway());
                arrival.setDelay(it.getDelay());
                arrival.setActualTime(it.getActualTime());
                arrival.setGate(it.getGate());
                arrival.setEstimatedRunway(it.getEstimatedRunway());
                arrival.setEstimatedTime(it.getEstimatedTime());
                arrival.setIataCode(it.getIataCode());
                arrival.setIcaoCode(it.getIcaoCode());
                arrival.setTerminal(it.getTerminal());
                arrival.setScheduledTime(it.getScheduledTime());
                flexLayout.add(new ArrivalView(new ArrivalAirportModel(UUID.fromString(it.getId()), it.getFlightCode(),arrival, it.getAirport()), serverApis, true));
            } else {
                Departure departure = new Departure();
                departure.setBaggage(it.getBaggage());
                departure.setActualRunway(it.getActualRunway());
                departure.setDelay(it.getDelay());
                departure.setActualTime(it.getActualTime());
                departure.setGate(it.getGate());
                departure.setEstimatedRunway(it.getEstimatedRunway());
                departure.setEstimatedTime(it.getEstimatedTime());
                departure.setIataCode(it.getIataCode());
                departure.setIcaoCode(it.getIcaoCode());
                departure.setTerminal(it.getTerminal());
                departure.setScheduledTime(it.getScheduledTime());
                flexLayout.add(new DepartureView(new DepartureAirportModel(UUID.fromString(it.getId()), it.getFlightCode(),departure, it.getAirport()), serverApis, true));

            }
        });
        setHorizontalComponentAlignment(Alignment.CENTER, flexLayout);
        add(flexLayout);
    }
}
