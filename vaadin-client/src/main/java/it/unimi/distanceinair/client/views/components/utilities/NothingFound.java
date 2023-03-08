package it.unimi.distanceinair.client.views.components.utilities;

import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.server.StreamResource;

public class NothingFound extends VerticalLayout {
    public NothingFound(boolean save) {
        if(!save) {
            Label label = new Label("No results for inserted data...");
            StreamResource imageResource = new StreamResource("nothing-found.png",
                    () -> getClass().getResourceAsStream("/nothing-found.png"));
            Image img = new Image(imageResource, "");
            setDefaultHorizontalComponentAlignment(Alignment.CENTER);
            add(label, img);
        } else {
            Label label = new Label("No saved flights founded");
            StreamResource imageResource = new StreamResource("nothing-found.png",
                    () -> getClass().getResourceAsStream("/nothing-found.png"));
            Image img = new Image(imageResource, "");
            setDefaultHorizontalComponentAlignment(Alignment.CENTER);
            add(label, img);
        }
    }
}
