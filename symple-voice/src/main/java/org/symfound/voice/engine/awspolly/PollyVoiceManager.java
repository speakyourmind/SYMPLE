/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.symfound.voice.engine.awspolly;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.SdkClientException;
import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.services.polly.AmazonPolly;
import static com.amazonaws.services.polly.AmazonPollyClient.builder;
import com.amazonaws.services.polly.AmazonPollyClientBuilder;
import com.amazonaws.services.polly.model.DescribeVoicesRequest;
import com.amazonaws.services.polly.model.DescribeVoicesResult;
import com.amazonaws.services.polly.model.Voice;
import static java.lang.System.getProperties;
import java.util.ArrayList;
import static java.util.Collections.unmodifiableMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import org.apache.log4j.Logger;
import static org.apache.log4j.Logger.getLogger;
import org.symfound.voice.builder.VoiceManager;
import static org.symfound.voice.engine.awspolly.PollyEngine.LANGUAGE_EN_US;

/**
 *
 * @author Javed Gangjee
 */
public class PollyVoiceManager extends VoiceManager {

    private static final String NAME = PollyVoiceManager.class.getName();
    private static final Logger LOGGER = getLogger(NAME);

    /**
     *
     */
    public List<Voice> voices;

    /**
     *
     */
    public AmazonPolly client;

    /**
     *
     */
    public static final String AWSSECRET_KEY = "aws.secretKey";

    /**
     *
     */
    public static final String SYSTEM_ACCESS_KEY = "aws.accessKeyId";

    /**
     *
     */
    public String region = "us-west-2";

    /**
     *
     */
    public String accessKey = "AKIAJTZRH3EI4JHGUHLA";

    /**
     *
     */
    public String secretKey = "qZgqYzYQv55i28KWFURbv6rc29Lmn0PkAPAcjBxJ";

    /**
     *
     * @return
     */
    public AmazonPolly loadClient() {
        if (client == null) {
            client = buildClient(region, accessKey, secretKey);
        }
        return client;
    }

    /**
     *
     * @param region
     * @param accessKey
     * @param secretKey
     * @return
     */
    public AmazonPolly buildClient(String region, String accessKey, String secretKey) {

        LOGGER.info("Building Amazon Polly TTS client");
        AmazonPollyClientBuilder awsClient = builder();
        awsClient.setRegion(region);

        setSystemProperties(accessKey, secretKey);

        DefaultAWSCredentialsProviderChain credentials = new DefaultAWSCredentialsProviderChain();
        awsClient.setCredentials(credentials);

        awsClient.setClientConfiguration(new ClientConfiguration());
        AmazonPolly amazonPolly = awsClient.build();

        return amazonPolly;
    }

    /**
     *
     * @param accessKey
     * @param secretKey
     */
    public void setSystemProperties(String accessKey, String secretKey) {
        Properties props = getProperties();
        props.setProperty(SYSTEM_ACCESS_KEY, accessKey);
        props.setProperty(AWSSECRET_KEY, secretKey);
    }

    /**
     *
     * @return
     */
    public List<Voice> getVoices(){
        if (voices == null) {
            // Create describe voices request.
            DescribeVoicesRequest request = new DescribeVoicesRequest().withLanguageCode(LANGUAGE_EN_US);
            // Synchronously ask Amazon Polly to describe available TTS voices.
            DescribeVoicesResult describeVoicesResult = loadClient().describeVoices(request);
            voices = describeVoicesResult.getVoices();
        }
        return voices;
    }

    private ObjectProperty<Voice> voice;

    /**
     * Sets the user's selection preference
     *
     * @param value
     */
    public void setVoice(Voice value) {
        voiceProperty().setValue(value);

    }

    /**
     * 
     * @param name 
     */
    public void setVoice(String name) {
        Integer voiceNumber = getVoiceMap().get(name);
        Voice selectedVoice = getVoices().get(voiceNumber);
        setVoice(selectedVoice);
    }

    /**
     * Get how the user wants to interact with the program
     *
     * @return selection mode
     */
    public Voice getVoice() {
        return voiceProperty().getValue();
    }

    /**
     * Defines the way in which the user interacts with the program, that is,
     * the selection mode.
     *
     * @return selection
     */
    public ObjectProperty<Voice> voiceProperty() {
        if (voice == null) {
            voice = new SimpleObjectProperty<>(getVoices().get(0));
        }
        return voice;
    }

    /**
     *
     */
    public Map<String, Integer> voiceMap;

    /**
     * 
     * @return 
     */
    public Map<String, Integer> getVoiceMap() {
        if (voiceMap == null) {
            voiceMap = buildVoiceMap();
        }
        return voiceMap;
    }

    /**
     * 
     * @return 
     */
    public Map<String, Integer> buildVoiceMap() {
        HashMap<String, Integer> hashMap = new HashMap<>();
        List<Voice> availableVoices = getVoices();
        for (int i = 0; i < availableVoices.size(); i++) {
            hashMap.put(availableVoices.get(i).getId(), i);
        }
        return unmodifiableMap(hashMap);
    }

    /**
     * 
     * @return 
     */
    @Override
    public List<String> getAvailableVoices() {
        List<String> voiceList = new ArrayList<>();
        getVoices().forEach((availableVoice) -> {
            voiceList.add(availableVoice.getName());
        });
        return voiceList;
    }

}
