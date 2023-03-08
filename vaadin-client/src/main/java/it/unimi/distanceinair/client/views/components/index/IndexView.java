package it.unimi.distanceinair.client.views.components.index;

import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import it.unimi.distanceinair.client.views.main.MainLayout;

@PageTitle("Homepage")
@Route(value = "", layout = MainLayout.class)
@AnonymousAllowed()
public class IndexView extends VerticalLayout {

    public IndexView() {
        setPadding(true);

        add(new H1("Welcome to Distance in Air!"));
        add(new H3("Here you can search for international flights, you must first register to use this service."));
        add(new H3("Don't try to inspect outgoing and ingoing requests, they are encrypted!"));
    }

}
