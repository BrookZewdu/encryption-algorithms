package com.example.application.views.otp;

import com.example.application.views.MainLayout;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.details.Details;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H5;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;

@PageTitle("One-Time Pad(OTP)")
@Route(value = "OTP", layout = MainLayout.class)
@RouteAlias(value = "", layout = MainLayout.class)
public class OTPView extends VerticalLayout {

    private TextField name;
    private Button sayHello;
    private static final int charLimit = 200;
    private TextArea encryptInputTextArea;
    private TextArea encryptOutputTextArea;
    private TextField encryptKey, decryptKey;
    private TextArea decryptInputTextArea, decryptOutputTextArea;

    public OTPView() {

        Span content = new Span(
                "The One-Time Pad (OTP) encryption algorithm is a highly secure technique that pairs a plaintext message with a random secret key (the pad). The key must be at least as long as the message. Each bit or character of the plaintext is encrypted by combining it with the corresponding bit or character from the pad using modular addition. OTP provides unbreakable security if used correctly, as brute-force attacks yield multiple legitimate plaintexts, making it impossible to determine the correct one. However, practical challenges include generating and securely distributing large quantities of random keys for each message. 🛡️🔑        ");
        content.setWidth("400px");
        content.getStyle().set("font-weight", "normal");
        content.getStyle().set("font-size", "16px");
        content.getStyle().set("max-width", "400px");
        Anchor source = new Anchor("https://en.wikipedia.org/wiki/One-time_pad", "...read more");
        // source.setTarget("_blank");
        source.getStyle().set("text-decoration", "none");
        source.getStyle().set("color", "#76ABAE");

        content.add(source);

        Details details = new Details("What is OTP?", content);
        // details.setOpened(true);
        add(
                details,
                getEncryptionLayout(),
                getDecryptionLayout());
    }

    private Component getDecryptionLayout() {

        VerticalLayout decryptionLayout = new VerticalLayout();
        HorizontalLayout decryptionTextArea = new HorizontalLayout();
        decryptionTextArea.add(getDecryptInputTextArea(), getDecryptOutputTextArea());
        decryptionTextArea.setJustifyContentMode(JustifyContentMode.CENTER);
        decryptionTextArea.setSizeFull();

        decryptionLayout.add(decryptionTextArea, getDecryptButtonLayout());

        return decryptionLayout;
    }

    private Component getDecryptButtonLayout() {
        decryptKey = new TextField("Key");
        decryptKey.setPlaceholder("Key...");
        decryptKey.setClearButtonVisible(true);
        // key.setWidth("400px");
        Button decryptButton = new Button("Decrypt");
        decryptButton.addClickListener(e -> {
            Notification.show("Decrypting...");

            String input = decryptInputTextArea.getValue();
            String inputKey = decryptKey.getValue();
            if (input.isEmpty()) {
                Notification.show("Input must not be empty");
                return;
            }

            if (inputKey.isEmpty()) {

                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < input.length(); i++) {
                    sb.append((char) (Math.random() * 26 + 'A'));
                }
                inputKey = sb.toString();
                decryptKey.setValue(inputKey);
                // return;
            }
            Notification.show("Input : " + input + " Key: " + inputKey);
            if (input.length() != inputKey.length()) {
                Notification.show("Input and key must have the same length");
                return;
            }

            String decryptedOutput = stringDecryption(input.toUpperCase(), inputKey.toUpperCase());
            decryptOutputTextArea.setValue(decryptedOutput.toString());
            Notification notification = Notification
                    .show("Decryption completed!");
            notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
            Notification.show("Output : " + decryptedOutput.toString());
        });

        Button copyButton = new Button("Copy output");
        copyButton.setIcon(VaadinIcon.COPY.create());
        copyButton.addClickListener(e -> {
            String outputText = decryptOutputTextArea.getValue();
            Notification.show("Copying: " + outputText);
            UI.getCurrent().getPage().executeJs("navigator.clipboard.writeText('" + outputText + "')");
            Notification.show("Copied to clipboard");
        });
        HorizontalLayout keyContainer = new HorizontalLayout(decryptKey, decryptButton, copyButton);
        keyContainer.setSizeFull();
        keyContainer.setJustifyContentMode(JustifyContentMode.CENTER);
        keyContainer.setAlignItems(Alignment.END);
        return keyContainer;
    }

    private Component getDecryptOutputTextArea() {
        decryptOutputTextArea = new TextArea();
        decryptOutputTextArea.setLabel("Output(Plain Text)");
        decryptOutputTextArea.setTooltipText("Tooltip text");
        // outputTextArea.setEnabled(false);
        decryptOutputTextArea.setReadOnly(true);
        decryptOutputTextArea.setValue("Decrypted Message...");
        decryptOutputTextArea.setWidth("400px");

        return decryptOutputTextArea;
    }

    private Component getDecryptInputTextArea() {
        // TODO Auto-generated method stub
        decryptInputTextArea = new TextArea();
        decryptInputTextArea.setLabel("Input(Cypher Text)");
        decryptInputTextArea.setPlaceholder("Message to be Encrypted...");
        decryptInputTextArea.setTooltipText("Tooltip text");
        decryptInputTextArea.setClearButtonVisible(true);
        decryptInputTextArea.setHeight("200px");
        decryptInputTextArea.setWidth("400px");

        return decryptInputTextArea;
    }

    private Component getEncryptButtonLayout() {
        encryptKey = new TextField("Key");
        encryptKey.setPlaceholder("Key...");
        encryptKey.setClearButtonVisible(true);
        // key.setWidth("400px");
        Button encryptButton = new Button("Encrypt");
        encryptButton.addClickListener(e -> {
            Notification.show("Encrypting...");

            String input = encryptInputTextArea.getValue();
            String inputKey = encryptKey.getValue();
            if (input.isEmpty()) {
                Notification.show("Input must not be empty");
                return;
            }
            if (inputKey.isEmpty()) {
                // generate a random key of the same length as the input and display it in the
                // key field
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < input.length(); i++) {
                    sb.append((char) (Math.random() * 26 + 'A'));
                }
                inputKey = sb.toString();
                encryptKey.setValue(inputKey);
                // return;
            }
            Notification.show("Input : " + input + " Key: " + inputKey);
            if (input.length() != inputKey.length()) {
                Notification.show("Input and key must have the same length");
                return;
            }

            String encryptedOutput = stringEncryption(input.toUpperCase(), inputKey.toUpperCase());
            encryptOutputTextArea.setValue(encryptedOutput.toString());
            Notification notification = Notification
                    .show("Encryption completed!");
            notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
            Notification.show("Output : " + encryptedOutput.toString());
        });

        Button copyButton = new Button("Copy output");
        copyButton.setIcon(VaadinIcon.COPY.create());
        // ...

        copyButton.addClickListener(e -> {
            String outputText = encryptOutputTextArea.getValue();
            Notification.show("Copying: " + outputText);
            UI.getCurrent().getPage().executeJs("navigator.clipboard.writeText('" + outputText + "')");
            Notification.show("Copied to clipboard");
        });
        HorizontalLayout keyContainer = new HorizontalLayout(encryptKey, encryptButton, copyButton);
        keyContainer.setSizeFull();
        keyContainer.setJustifyContentMode(JustifyContentMode.CENTER);
        keyContainer.setAlignItems(Alignment.END);
        return keyContainer;

    }

    public static String stringEncryption(String text,
            String key) {
        String cipherText = "";
        int cipher[] = new int[key.length()];

        for (int i = 0; i < key.length(); i++) {
            cipher[i] = text.charAt(i) - 'A'
                    + key.charAt(i)
                    - 'A';
        }
        for (int i = 0; i < key.length(); i++) {
            if (cipher[i] > 25) {
                cipher[i] = cipher[i] - 26;
            }
        }
        for (int i = 0; i < key.length(); i++) {
            int x = cipher[i] + 'A';
            cipherText += (char) x;
        }
        return cipherText;
    }

    public static String stringDecryption(String s,
            String key) {

        String plainText = "";

        int plain[] = new int[key.length()];

        for (int i = 0; i < key.length(); i++) {
            plain[i] = s.charAt(i) - 'A'
                    - (key.charAt(i) - 'A');
        }
        for (int i = 0; i < key.length(); i++) {
            if (plain[i] < 0) {
                plain[i] = plain[i] + 26;
            }
        }
        for (int i = 0; i < key.length(); i++) {
            int x = plain[i] + 'A';
            plainText += (char) x;
        }
        return plainText;
    }

    private Component getEncryptionLayout() {

        VerticalLayout encryptionLayout = new VerticalLayout();
        HorizontalLayout encryptionTextArea = new HorizontalLayout();
        encryptionTextArea.add(getInputTextArea(), getOutputTextArea());
        encryptionTextArea.setJustifyContentMode(JustifyContentMode.CENTER);
        encryptionTextArea.setSizeFull();

        encryptionLayout.add(encryptionTextArea, getEncryptButtonLayout());
        return encryptionLayout;
    }

    private Component getOutputTextArea() {
        encryptOutputTextArea = new TextArea();
        encryptOutputTextArea.setLabel("Output(Cypher Text)");
        encryptOutputTextArea.setTooltipText("Tooltip text");
        encryptOutputTextArea.setReadOnly(true);
        encryptOutputTextArea.setValue("Decrypted Message...");
        encryptOutputTextArea.setWidth("400px");

        return encryptOutputTextArea;
    }

    private Component getInputTextArea() {

        encryptInputTextArea = new TextArea();
        encryptInputTextArea.setLabel("Input(Plain Text)");
        encryptInputTextArea.setPlaceholder("Message to be Encrypted...");
        encryptInputTextArea.setTooltipText("Tooltip text");
        encryptInputTextArea.setClearButtonVisible(true);
        encryptInputTextArea.setHeight("200px");
        encryptInputTextArea.setWidth("400px");

        return encryptInputTextArea;
    }

}
