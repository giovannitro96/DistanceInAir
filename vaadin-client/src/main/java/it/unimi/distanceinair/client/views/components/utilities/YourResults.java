package it.unimi.distanceinair.client.views.components.utilities;

import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.server.StreamResource;

public class YourResults extends VerticalLayout {
    public YourResults() {
        Label label = new Label("Your results");
        Icon icon = new Icon(VaadinIcon.ARROW_DOWN);
        setDefaultHorizontalComponentAlignment(Alignment.CENTER);
        add(label, icon);
    }
}
