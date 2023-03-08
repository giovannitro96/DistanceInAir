package it.unimi.distanceinair.client.views.components.utilities;

import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;

public class HorizontallyAlignedView extends HorizontalLayout {
    public HorizontallyAlignedView(String name, String value) {
        setAlignItems(Alignment.AUTO);
        Label label1 = new Label(name+":");
        label1.getStyle().set("font-weight", "bold");
        Label label2 = new Label(value);
        add(label1, label2);
    }
}
