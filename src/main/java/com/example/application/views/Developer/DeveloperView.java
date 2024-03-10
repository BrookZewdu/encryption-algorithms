package com.example.application.views.Developer;

import com.example.application.views.MainLayout;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.avatar.Avatar;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.html.H5;
import com.vaadin.flow.component.html.H6;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.StreamResource;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.html.Anchor;

@PageTitle("Developer")
@Route(value = "Developer", layout = MainLayout.class)
public class DeveloperView extends VerticalLayout {

    public DeveloperView() {

        setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        setAlignItems(FlexComponent.Alignment.CENTER);
        getStyle().set("background-color", "#EEEEEE");

        Image image = new Image(
                "https://res.cloudinary.com/eskalate/image/upload/dpr_auto,f_auto,q_auto/v1/team/biruk_z",
                "My Alt Image");
        image.getStyle().set("border-radius", "50%");

        // Set the desired width and height
        image.setWidth("300px");
        image.setHeight("200px");
        H1 name = new H1("Biruk Zewdu");
        name.getStyle().set("color", "#222831");
        /// add a description for the developer right after the name
        H5 title = new H5("Head of Academy at ");
        Anchor a2svLink = new Anchor("https://a2sv.org", "A2SV");
        a2svLink.getStyle().set("text-decoration", "none");
        a2svLink.getStyle().set("color", "#76ABAE");
        title.add(a2svLink);
        title.getStyle().set("color", "#31363F");
        H5 description = new H5(
                "A 5th year Computer and Electrical Engineering student at Addis Ababa Institute of Technology.");
        description.getStyle().set("color", "#31363F");
        // description.getStyle().set("font-size", "20px");
        description.getStyle().set("text-align", "center");
        description.getStyle().set("margin-top", "10px");
        description.getStyle().set("margin-bottom", "10px");
        // the text in the description should only span a few pixels wide
        description.getStyle().set("max-width", "500px");

        Button contactDeveloper = new Button("Contact Developer");
        contactDeveloper.addClickShortcut(Key.ENTER);
        contactDeveloper.getStyle().set("background-color", "#76ABAE");
        contactDeveloper.getStyle().set("color", "white");
        contactDeveloper.getStyle().set("border-radius", "5px");
        contactDeveloper.getStyle().set("margin-top", "10px");
        contactDeveloper.getStyle().set("margin-bottom", "10px");
        contactDeveloper.getStyle().set("padding", "10px");
        contactDeveloper.getStyle().set("border", "none");
        contactDeveloper.getStyle().set("cursor", "pointer");
        contactDeveloper.getStyle().set("font-size", "16px");
        contactDeveloper.getStyle().set("font-weight", "bold");
        contactDeveloper.getStyle().set("text-align", "center");
        contactDeveloper.getStyle().set("text-decoration", "none");
        contactDeveloper.getStyle().set("display", "inline-block");
        contactDeveloper.getStyle().set("transition-duration", "0.4s");
        contactDeveloper.setId("contact-developer");

        Anchor telegramLink = new Anchor("https://t.me/BirukZewdu", contactDeveloper);
        telegramLink.getStyle().set("text-decoration", "none");
        telegramLink.getStyle().set("color", "blue");

        setSizeFull();
        add(image, name, title, description, telegramLink);

    }

}
