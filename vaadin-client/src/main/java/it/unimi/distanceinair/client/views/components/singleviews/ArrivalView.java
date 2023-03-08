package it.unimi.distanceinair.client.views.components.singleviews;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import it.unimi.distanceinair.client.domain.model.ArrivalAirportModel;
import it.unimi.distanceinair.client.domain.model.DepartureAirportModel;
import it.unimi.distanceinair.client.domain.xml.Airport;
import it.unimi.distanceinair.client.domain.xml.Arrival;
import it.unimi.distanceinair.client.domain.xml.DistanceInAir;
import it.unimi.distanceinair.client.domain.xml.Flight;
import it.unimi.distanceinair.client.service.ServerApis;
import it.unimi.distanceinair.client.util.ViewsUtils;
import it.unimi.distanceinair.client.views.components.homepage.HomepageView;
import it.unimi.distanceinair.client.views.components.utilities.HorizontallyAlignedView;
import it.unimi.distanceinair.client.views.components.utilities.NothingFound;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.vaadin.flow.theme.lumo.LumoUtility.Background.SUCCESS_10;

public class ArrivalView extends VerticalLayout {


    ArrivalAirportModel arrivalAirportModelSave;
    Button star;
    public ArrivalView(ArrivalAirportModel arrivalAirport, ServerApis serverApis, boolean isSaved) {
        star = new Button(new Icon(VaadinIcon.STAR));
        star.addClickListener(e -> {
            try {
                ViewsUtils.forceRefreshToken();
                DistanceInAir distanceInAir = new DistanceInAir();
                distanceInAir.setArrival(arrivalAirportModelSave.getArrival());
                Flight flightForCode = new Flight();
                flightForCode.setIataNumber(arrivalAirport.getCode());
                distanceInAir.setFlight(flightForCode);
                arrivalAirportModelSave.setId(UUID.fromString(serverApis.saveFlightIntoPref(distanceInAir, ViewsUtils.getToken())));
                Notification notification = new Notification();
                notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
                notification.setText("Flight with code " + arrivalAirport.getCode() + " saved successfully.");
                notification.setDuration(5000);
                notification.open();
            } catch (Exception ex) {
                Notification notification = new Notification();
                notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
                notification.setText(ex.getMessage());
                notification.setDuration(5000);
                notification.open();
            }
        });
        arrivalAirportModelSave = arrivalAirport;
        Label typeLabel;
        List<Component> components = new ArrayList<>();
        if(!isSaved) {
            components.add(star);
        }
        Airport airport = arrivalAirport.getAirport();
        Arrival arrival = arrivalAirport.getArrival();

        typeLabel = new Label("Arrival");

        Button close = new Button();

        if(isSaved) {
            close.setIcon(new Icon(VaadinIcon.TRASH));
            ConfirmDialog dialog = new ConfirmDialog();
            dialog.setHeader("Unsave this flight?");
            dialog.setText(
                    "Are you sure you want to permanently remove this flight from saveds?");

            dialog.setCancelable(true);

            dialog.setConfirmText("Delete");
            dialog.setConfirmButtonTheme("error primary");
            dialog.addConfirmListener(event -> {
                try {
                    ViewsUtils.forceRefreshToken();
                    serverApis.removeFlightFromPref(ViewsUtils.getToken(), arrivalAirport.getId());
                    HorizontalLayout parent = (HorizontalLayout) this.getParent().get();
                    parent.remove(this);
                    Notification notification = new Notification();
                    notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
                    notification.setText("Saved flight removed successfully");
                    notification.setDuration(5000);
                    notification.open();
                    if(parent.getComponentCount()==0) {
                        parent.removeAll();
                        NothingFound nf = new NothingFound(true);
                        parent.add(nf);
                        parent.setAlignItems(Alignment.CENTER);
                    }
                } catch (Exception ex) {
                    Notification notification = new Notification();
                    notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
                    notification.setText(ex.getMessage());
                    notification.setDuration(5000);
                    notification.open();
                }
            });

            close.addClickListener(event -> {
                dialog.open();
            });
        } else {
            close.setIcon(new Icon(VaadinIcon.CLOSE));
            close.addClickListener(e -> {
                HorizontalLayout parent = (HorizontalLayout) this.getParent().get();
                parent.remove(this);

            });
        }


        close.getStyle().set("bottom", "1vh");

        typeLabel.getStyle().set("font-weight", "bold");
        typeLabel.getStyle().set("font-size", "130%");
        HorizontalLayout labelClose = new HorizontalLayout();
        labelClose.setJustifyContentMode(JustifyContentMode.BETWEEN);
        labelClose.setWidthFull();
        labelClose.setAlignItems(Alignment.AUTO);
        Icon vaadinIcon = new Icon(VaadinIcon.AIRPLANE);
        vaadinIcon.getStyle().set("transform", "rotate(90deg)");
        labelClose.add(vaadinIcon, typeLabel, close);
        add(labelClose);
        HorizontallyAlignedView horiz = new HorizontallyAlignedView("Flight number: ", arrivalAirport.getCode());
        components.add(horiz);

        Icon positionIcon = new Icon(VaadinIcon.MAP_MARKER);
        positionIcon.setTooltipText("Airport position");
        positionIcon.addClickListener(e -> {
            UI.getCurrent().getPage().executeJs("window.open(\"https://maps.google.com/?q="+ airport.getLatitude()+","+ airport.getLongitude()+"\");");
        });
        HorizontallyAlignedView scheduledTime = new HorizontallyAlignedView("Scheduled runaway time", ViewsUtils.formatDate(arrival.getScheduledTime()));
        components.add(scheduledTime);
        if(arrival.getDelay() != null && !arrival.getDelay().isBlank() && !arrival.getDelay().equals("0")) {
            HorizontallyAlignedView delayView = new HorizontallyAlignedView("Flight delay time", arrival.getDelay());
            components.add(delayView);
        }
        if(arrival.getDelay() != null && !arrival.getDelay().isBlank()) {
            HorizontallyAlignedView actualTimeView = new HorizontallyAlignedView("Actual runaway time", ViewsUtils.formatDate(arrival.getActualTime()));
            components.add(actualTimeView);
        }
        HorizontallyAlignedView airportView = new HorizontallyAlignedView("Airport", airport.getName() + ", " + airport.getCountry());
        airportView.add(positionIcon);

        components.add(airportView);
        if(arrival.getTerminal() != null && !arrival.getTerminal().isBlank() ) {
            HorizontallyAlignedView terminalView = new HorizontallyAlignedView("Terminal", arrival.getTerminal());
            components.add(terminalView);
        }

        components.forEach(it -> setHorizontalComponentAlignment(Alignment.CENTER, it));
        setHorizontalComponentAlignment(Alignment.END, close);
        addClassName(SUCCESS_10);
        getStyle().set("border-radius", "0.5em");
        getStyle().set("padding","25px");
        setWidth("30vw");
        getStyle().set("border", "solid grey");
        getStyle().set("background-color", "#335C81");
        components.forEach(this::add);

    }

}
