package org.symfound.controls.user.media.youtube;

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
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.YouTubeScopes;
import com.google.api.services.youtube.model.*;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.apache.log4j.Logger;

/**
 *
 * @author Administrator
 */
public class YouTubePlaylistLoader {

    private static final String NAME = YouTubePlaylistLoader.class.getName();

    /**
     *
     */
    public static final Logger LOGGER = Logger.getLogger(NAME);

    /**
     * Application name.
     */
    private static final String APPLICATION_NAME = "API Sample";

    /**
     * Directory to store user credentials for this application.
     */
    private static final java.io.File DATA_STORE_DIR = new java.io.File(
            System.getProperty("user.home"), ".credentials/youtube-java-quickstart");

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
     * ~/.credentials/drive-java-quickstart
     */
    private static final List<String> SCOPES
            = Arrays.asList(YouTubeScopes.YOUTUBE_READONLY);

    static {
        try {
            HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
            DATA_STORE_FACTORY = new FileDataStoreFactory(DATA_STORE_DIR);
        } catch (IOException | GeneralSecurityException t) {
            System.exit(1);
        }
    }

    /**
     * Create an authorized Credential object.
     *
     * @return an authorized Credential object.
     * @throws IOException
     */
    public static Credential authorize() throws IOException {
        // Load client secrets.
        InputStream in
                = YouTubePlaylistLoader.class.getResourceAsStream("/client_secret.json");
        GoogleClientSecrets clientSecrets
                = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

        // Build flow and trigger user authorization request.
        GoogleAuthorizationCodeFlow flow
                = new GoogleAuthorizationCodeFlow.Builder(
                        HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
                        .setDataStoreFactory(DATA_STORE_FACTORY)
                        .setAccessType("offline")
                        .build();
        Credential credential = new AuthorizationCodeInstalledApp(
                flow, new LocalServerReceiver()).authorize("user");
        return credential;
    }

    /**
     * Build and return an authorized API client service, such as a YouTube Data
     * API client service.
     *
     * @return an authorized API client service
     * @throws IOException
     */
    public static YouTube getYouTubeService() throws IOException {
        Credential credential = authorize();
        return new YouTube.Builder(HTTP_TRANSPORT, JSON_FACTORY, credential)
                .setApplicationName(APPLICATION_NAME)
                .build();
    }

    /**
     *
     * @param playlistId
     * @return
     * @throws IOException
     */
    public static List<String> load(String playlistId) throws IOException {

        YouTube youtube = getYouTubeService();

        HashMap<String, String> parameters = new HashMap<>();
        parameters.put("part", "contentDetails");
        parameters.put("maxResults", "50");
        parameters.put("playlistId", playlistId);

        YouTube.PlaylistItems.List playlistItemsListByPlaylistIdRequest = youtube.playlistItems().list(parameters.get("part"));
        if (parameters.containsKey("maxResults")) {
            playlistItemsListByPlaylistIdRequest.setMaxResults(Long.parseLong(parameters.get("maxResults")));
        }

        if (parameters.containsKey("playlistId") && !parameters.get("playlistId").isEmpty()) {
            playlistItemsListByPlaylistIdRequest.setPlaylistId(parameters.get("playlistId"));
        }
        List<PlaylistItem> playlistItemList = new ArrayList<>();
        String nextToken = "";

        // Call the API one or more times to retrieve all items in the
        // list. As long as the API response returns a nextPageToken,
        // there are still more items to retrieve.
        do {
            playlistItemsListByPlaylistIdRequest.setPageToken(nextToken);
            PlaylistItemListResponse playlistItemResult = playlistItemsListByPlaylistIdRequest.execute();

            playlistItemList.addAll(playlistItemResult.getItems());

            nextToken = playlistItemResult.getNextPageToken();
        } while (nextToken != null);

        List<String> playlist = new ArrayList<>();
        for (int i = 0; i < playlistItemList.size(); i++) {
            playlist.add(playlistItemList.get(i).getContentDetails().getVideoId());
        }

        LOGGER.info(playlist.size() + " videos loaded from playlist " + playlistId);

        return playlist;
    }

    /**
     *
     */
    public void start() {
        try {
            List<String> load = load(getPlaylistID());
            setContents(load);
        } catch (IOException ex) {
            LOGGER.fatal(ex);
        }
    }
    
      private StringProperty playlistID;

    /**
     *
     * @param value
     */
    public void setPlaylistID(String value) {
        playlistIDProperty().setValue(value);
        LOGGER.info("Playlist set to: " + value);

    }

    /**
     *
     * @return
     */
    public String getPlaylistID() {
        return playlistIDProperty().getValue();
    }

    /**
     *
     * @return
     */
    public StringProperty playlistIDProperty() {
        if (playlistID == null) {
            playlistID = new SimpleStringProperty();
        }
        return playlistID;
    }
    
    private ListProperty<String> contents;

    /**
     *
     * @param value
     */
    public void setContents(List<String> value) {
        ObservableList<String> list = FXCollections.observableArrayList(value);
        contentProperty().setValue(list);
    }

    /**
     *
     * @return
     */
    public List<String> getContents() {
        return contentProperty().getValue();
    }

    /**
     *
     * @return
     */
    public ListProperty<String> contentProperty() {
        if (contents == null) {
            contents = new SimpleListProperty<>();
        }
        return contents;
    }

}
