package com.example.application.views.tripledes;

import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import com.example.application.views.MainLayout;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.details.Details;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@PageTitle("Triple DES")
@Route(value = "triple-DES", layout = MainLayout.class)
public class TripleDESView extends VerticalLayout {

    private static final int charLimit = 200;
    private TextArea encryptInputTextArea;
    private TextArea encryptOutputTextArea;
    private TextField encryptKey, decryptKey;
    private TextArea decryptInputTextArea, decryptOutputTextArea;

    public TripleDESView() {
        Span content = new Span(
                "The Triple Data Encryption Algorithm (3DES) is a symmetric-key block cipher that enhances the security of the original Data Encryption Standard (DES). It achieves this by applying the DES algorithm three times consecutively with different keys. Each operation uses a 56-bit key, resulting in a total key length of 168 bits. Despite its effectiveness, 3DES is considered slow compared to modern encryption algorithms. For new applications, it’s recommended to use Advanced Encryption Standard (AES) with 128-bit or 256-bit keys for better performance and security. 🛡️🔑");
        content.setWidth("400px");
        content.getStyle().set("font-weight", "normal");
        content.getStyle().set("font-size", "16px");
        content.getStyle().set("max-width", "400px");
        Anchor source = new Anchor("https://en.wikipedia.org/wiki/Triple_DES", "...read more");
        // source.setTarget("_blank");
        source.getStyle().set("text-decoration", "none");
        source.getStyle().set("color", "#76ABAE");

        content.add(source);

        Details details = new Details("What is Triple DES?", content);
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

        Button decryptButton = new Button("Decrypt");
        decryptButton.addClickListener(e -> {
            Notification.show("Decrypting...");

            String input = decryptInputTextArea.getValue();
            String inputKey = decryptKey.getValue();
            if (input.isEmpty() || inputKey.isEmpty()) {
                Notification.show("Input or key must not be empty");
                return;
            }

            if (inputKey.length() < 24 || inputKey.length() > 24) {
                Notification.show("Key must be 24 characters long");
                return;
            }

            Notification.show("Input : " + input + " Key: " + inputKey);

            String decryptedOutput = "ERROR";
            try {
                decryptedOutput = stringDecryption(input, inputKey);
            } catch (InvalidKeyException | IllegalBlockSizeException | BadPaddingException
                    | InvalidAlgorithmParameterException | NoSuchAlgorithmException | NoSuchPaddingException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
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
            if (input.isEmpty() || inputKey.isEmpty()) {
                Notification.show("Input and key must not be empty");
                return;
            }

            if (inputKey.length() < 24 || inputKey.length() > 24) {
                Notification.show("Key must be 24 characters long");
                return;
            }
            Notification.show("Input : " + input + " Key: " + inputKey);

            String encryptedOutput = "Error";
            try {
                encryptedOutput = stringEncryption(input, inputKey);
            } catch (InvalidKeyException | NoSuchAlgorithmException | NoSuchPaddingException
                    | InvalidAlgorithmParameterException | IllegalBlockSizeException | BadPaddingException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
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

    public static String stringEncryption(String text, String key)
            throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException,
            InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
        byte[] secretKey = key.getBytes();

        SecretKeySpec secretKeySpec = new SecretKeySpec(secretKey, "TripleDES");
        byte[] iv = "a76nb5h9".getBytes();
        IvParameterSpec ivSpec = new IvParameterSpec(iv);

        String secretMessage = text;
        Cipher encryptCipher = Cipher.getInstance("TripleDES/CBC/PKCS5Padding");
        encryptCipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivSpec);
        byte[] secretMessagesBytes = secretMessage.getBytes(StandardCharsets.UTF_8);
        byte[] encryptedMessageBytes = encryptCipher.doFinal(secretMessagesBytes);
        String encodedMessage = Base64.getEncoder().encodeToString(encryptedMessageBytes);

        return encodedMessage;
    }

    public static String stringDecryption(String s, String key) throws IllegalBlockSizeException, BadPaddingException,
            InvalidKeyException, InvalidAlgorithmParameterException, NoSuchAlgorithmException, NoSuchPaddingException {

        byte[] secretKey = key.getBytes();
        Cipher decryptCipher = Cipher.getInstance("TripleDES/CBC/PKCS5Padding");
        SecretKeySpec secretKeySpec = new SecretKeySpec(secretKey, "TripleDES");
        byte[] iv = "a76nb5h9".getBytes();
        IvParameterSpec ivSpec = new IvParameterSpec(iv);
        decryptCipher.init(Cipher.DECRYPT_MODE, secretKeySpec, ivSpec);
        byte[] decryptedMessageBytes = decryptCipher.doFinal(Base64.getDecoder().decode(s));
        String decryptedMessage = new String(decryptedMessageBytes, StandardCharsets.UTF_8);
        return decryptedMessage;

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
