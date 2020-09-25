package org.symfound.social.google.gmail;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.Gmail.Users.Messages;
import com.google.api.services.gmail.GmailScopes;
import com.google.api.services.gmail.model.*;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.List;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author Javed Gangjee
 */


public class GmailReader {

    // SECURITY TO DO: Secure gmail credentials
    /**
     * Application name.
     */
    private static final String APPLICATION_NAME = "Gmail";

    /**
     * Directory to store user credentials for this application.
     */
    private static final java.io.File DATA_STORE_DIR = new java.io.File(
            System.getProperty("user.home"), ".credentials/gmail-java");

    /**
     * Global instance of the {@link FileDataStoreFactory}.
     */
    private static FileDataStoreFactory DATA_STORE_FACTORY;

    /**
     * Global instance of the JSON factory.
     */
    private static final JsonFactory JSON_FACTORY
            = JacksonFactory.getDefaultInstance();

    /**
     * Global instance of the HTTP transport.
     */
    private static HttpTransport HTTP_TRANSPORT;

    /**
     * Global instance of the scopes required by this quickstart.
     *
     * If modifying these scopes, delete your previously saved credentials at
     * ~/.credentials/gmail-java-quickstart
     */
    private static final List<String> SCOPES = new ArrayList<>(GmailScopes.all());

    static {
        try {
            HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
            DATA_STORE_FACTORY = new FileDataStoreFactory(DATA_STORE_DIR);
        } catch (IOException | GeneralSecurityException t) {
            System.exit(1);
        }
    }

    /**
     * Creates an authorized Credential object.
     *
     * @return an authorized Credential object.
     * @throws IOException
     */
    public static Credential authorize() throws IOException {
        // Load client secrets.
        InputStream in = GmailReader.class.getResourceAsStream("/client_id.json");
        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

        // Build flow and trigger user authorization request.
        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
                .setDataStoreFactory(DATA_STORE_FACTORY)
                .setAccessType("offline")
                .build();
        Credential credential = new AuthorizationCodeInstalledApp(
                flow, new LocalServerReceiver()).authorize("user");
        return credential;
    }

    /**
     * Build and return an authorized Gmail client service.
     *
     * @return an authorized Gmail client service
     * @throws IOException
     */
    public static Gmail getGmailService() throws IOException {
        Credential credential = authorize();
        return new Gmail.Builder(HTTP_TRANSPORT, JSON_FACTORY, credential)
                .setApplicationName(APPLICATION_NAME)
                .build();
    }

    /**
     *
     * @param query
     * @param max
     * @return
     * @throws IOException
     */
    public static List<String> getMessages(String query, Integer max) throws IOException {

        // Build a new authorized API client service.
        Gmail service = getGmailService();
        // Print the labels in the user's account.
        String user = "me";
        //  Messages messages = service.users().messages();
        //  Messages.List list = messages.list(user);
        Messages messages = service.users().messages();
        ListMessagesResponse response = messages.list(user).setQ(query).execute();

        List<Message> messagesList = new ArrayList<>();
        while (response.getMessages() != null) {
            messagesList.addAll(response.getMessages());
            if (response.getNextPageToken() != null) {
                String pageToken = response.getNextPageToken();
                response = messages.list(user).setQ(query).setPageToken(pageToken).execute();
            } else {
                break;
            }
        }
        List<String> messagesAsString = new ArrayList<>();
        List<Message> retrieveMessages;
        if (messagesList.size() > max) {
            retrieveMessages = new ArrayList<>(messagesList.subList(0, max));
        } else{
            retrieveMessages = new ArrayList<>(messagesList);
        }

        for (Message message : retrieveMessages) {
            Message message1 = messages.get(user, message.getId()).setFormat("full").execute();

            GMailGetter getter = new GMailGetter();
            String content = getter.getContent(message1);
            messagesAsString.add(content);

        }
        return messagesAsString;
    }

    private ListProperty<String> mail;

    /**
     *
     * @param value
     */
    public void setMail(List<String> value) {
        ObservableList<String> list = FXCollections.observableArrayList(value);
        mailProperty().setValue(list);
    }

    /**
     *
     * @return
     */
    public List<String> getMail() {
        return mailProperty().getValue();
    }

    /**
     *
     * @return
     */
    public ListProperty<String> mailProperty() {
        if (mail == null) {
            mail = new SimpleListProperty<>();
        }
        return mail;
    }
}
