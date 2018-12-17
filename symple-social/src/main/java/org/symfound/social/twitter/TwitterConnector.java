/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.symfound.social.twitter;

import java.awt.Desktop;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.prefs.Preferences;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import org.symfound.social.SocialMediaConnector;
import static org.symfound.social.twitter.TwitterReader.LOGGER;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;
import twitter4j.conf.ConfigurationBuilder;

/**
 *
 * @author Javed
 */
public class TwitterConnector extends SocialMediaConnector {

    private Twitter twitter;

    // SECURITY TO DO: Secure Oath and secret
    public Twitter getTwitterFactory() {
        if (twitter == null) {
            twitter = new TwitterFactory(new ConfigurationBuilder()
                    .setTweetModeExtended(true)
                    .setOAuthConsumerKey("xaEOSvSaWWPnPUvfJEbUJG9zt")
                    .setOAuthConsumerSecret("Vo7im46M1otYydRWUSGzxTYdPBdee9GmD3P2fug8x1fmdHwuVu")
                    .build()).getInstance();
        }
        return twitter;
    }

    AccessToken accessToken = null;

    public void connect() throws IOException, TwitterException, URISyntaxException {
        if (getToken().isEmpty() || getTokenSecret().isEmpty()) {
            // The factory instance is re-useable and thread safe.

            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            //      while (null == accessToken) {
            System.out.println("Open the following URL and grant access to your account:");
            System.out.println(getRequestToken().getAuthorizationURL());
            Desktop.getDesktop().browse(new URL(getRequestToken().getAuthorizationURL()).toURI());

            //  System.out.print("Enter the PIN(if aviailable) or just hit enter.[PIN]:");
            //  setPin(br.readLine());
            pinProperty().addListener((observable, oldValue, newValue) -> {
                try {
                    if (getPin().length() > 0) {
                        accessToken = getTwitterFactory().getOAuthAccessToken(getRequestToken(), getPin());

                    } else {
                        accessToken = getTwitterFactory().getOAuthAccessToken();
                    }
                } catch (TwitterException te) {
                    if (401 == te.getStatusCode()) {
                        System.out.println("Unable to get the access token.");
                    } else {
                        te.printStackTrace();
                    }
                }
            });

            //    }
            //persist to the accessToken for future reference.
            storeAccessToken(twitter.verifyCredentials().getId(), accessToken);
        } else {
            getTwitterFactory().setOAuthAccessToken(new AccessToken(getToken(), getTokenSecret()));
        }
    }
    private RequestToken requestToken;

    public RequestToken getRequestToken() {
        if (requestToken == null) {
            try {
                requestToken = getTwitterFactory().getOAuthRequestToken();
            } catch (TwitterException ex) {
                Logger.getLogger(TwitterReader.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return requestToken;
    }

    private void storeAccessToken(long useId, AccessToken accessToken) {
        setToken(accessToken.getToken());
        setTokenSecret(accessToken.getTokenSecret());
    }

    private StringProperty pin;

    /**
     *
     * @param value
     */
    public void setPin(String value) {
        pinProperty().set(value);
    }

    /**
     *
     * @return
     */
    public String getPin() {
        return pinProperty().get();
    }

    /**
     *
     * @return
     */
    public StringProperty pinProperty() {
        if (pin == null) {
            pin = new SimpleStringProperty("");
        }
        return pin;
    }

    private static final String TOKEN_KEY = "token";
    private static final String DEFAULT_TOKEN = "";
    private StringProperty token;

    /**
     *
     * @param value
     */
    public void setToken(String value) {
        tokenProperty().setValue(value);
        getPreferences().put("token", value);
        LOGGER.info("Token set to: " + value);
    }

    /**
     *
     * @return
     */
    public String getToken() {
        return tokenProperty().getValue();
    }

    /**
     *
     * @return
     */
    public StringProperty tokenProperty() {
        //   if (token == null) {
        token = new SimpleStringProperty(getPreferences().get(TOKEN_KEY, DEFAULT_TOKEN));
        // }
        return token;
    }

    private static final String TOKEN_SECRET_KEY = "tokenSecret";
    private static final String DEFAULT_TOKEN_SECRET = "";
    private StringProperty tokenSecret;

    /**
     *
     * @param value
     */
    public void setTokenSecret(String value) {
        tokenSecretProperty().setValue(value);
        getPreferences().put("tokenSecret", value);
        LOGGER.info("TokenSecret set to: " + value);

    }

    /**
     *
     * @return
     */
    public String getTokenSecret() {
        return tokenSecretProperty().getValue();
    }

    /**
     *
     * @return
     */
    public StringProperty tokenSecretProperty() {
        //   if (tokenSecret == null) {
        tokenSecret = new SimpleStringProperty(getPreferences().get(TOKEN_SECRET_KEY, DEFAULT_TOKEN_SECRET));
        // }
        return tokenSecret;
    }

    Preferences preferences;

    public Preferences getPreferences() {
        if (preferences == null) {
            String name = "reader";
            Class<? extends TwitterConnector> aClass = this.getClass();
            preferences = Preferences.userNodeForPackage(aClass).node(name);
        }
        return preferences;
    }
}
