package it.unimi.distanceinair.client.views.components.saved;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import it.unimi.distanceinair.client.domain.xml.FlightDto;
import it.unimi.distanceinair.client.service.ServerApis;
import it.unimi.distanceinair.client.util.ViewsUtils;
import it.unimi.distanceinair.client.views.components.saved.internal.SavedFlightsComponent;
import it.unimi.distanceinair.client.views.components.utilities.NothingFound;
import it.unimi.distanceinair.client.views.components.utilities.YourResults;
import it.unimi.distanceinair.client.views.main.MainLayout;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import javax.annotation.security.RolesAllowed;
import java.util.ArrayList;
import java.util.List;

@PageTitle("Saved flights")
@Route(value = "saved", layout = MainLayout.class)
@RolesAllowed("USER")
@SpringComponent
@UIScope
public class SavedFlights extends HorizontalLayout {

    @Autowired
    ServerApis flightApi;

    SavedFlights() {
        setJustifyContentMode(JustifyContentMode.CENTER);
        getStyle().set("flex-wrap", "wrap");
        getStyle().set("display","flex");
        setSpacing(true);
    }



    @Override
    protected void onAttach(AttachEvent attachEvent) {
        super.onAttach(attachEvent);
        ViewsUtils.forceRefreshToken();
        List<FlightDto> list = new ArrayList<>(flightApi.getAllSavedFlights(ViewsUtils.getToken()));
        if(list.isEmpty()) {
            NothingFound nf = new NothingFound(true);
            add(nf);
        } else {
            SavedFlightsComponent savedFlightsComponent = new SavedFlightsComponent(list, flightApi);
            removeAll();
            add(savedFlightsComponent);
        }
    }

}
