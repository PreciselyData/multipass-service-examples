package com.precisely.addressing.multipass.demo.custom;

import com.fasterxml.jackson.databind.JsonNode;
import com.precisely.addressing.AddressingBuilder;
import com.precisely.addressing.configuration.AddressingConfigurationBuilder;
import com.precisely.addressing.configuration.datasources.FileDataSource;
import com.precisely.addressing.multipass.demo.util.SDKConfigurations;
import com.precisely.addressing.v1.Addressing;
import com.precisely.addressing.v1.AddressingException;
import com.precisely.addressing.v1.Preferences;
import com.precisely.addressing.v1.model.FactoryDescriptionBuilder;
import com.precisely.addressing.v1.model.PreferencesBuilder;
import com.precisely.addressing.v1.model.RequestAddress;
import com.precisely.addressing.v1.model.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Paths;

import static com.precisely.addressing.multipass.sdk.util.JsonUtilities.mapper;

public class AddressingEngine {
    private static final Logger log = LoggerFactory.getLogger(AddressingEngine.class);
    private String dataPath = SDKConfigurations.getString("data.path");
    private String resourcesPath = SDKConfigurations.getString("resources.path");

    private static final AddressingEngine instance = instantiate();
    private Preferences defaultPreferences;

    public static AddressingEngine getInstance() {
        return instance;
    }

    private static AddressingEngine instantiate() {
        try {
            return new AddressingEngine();
        } catch (AddressingException e) {
            throw new RuntimeException(e);
        }
    }

    Addressing addressing;

    private AddressingEngine() throws AddressingException {
        addressing = new AddressingBuilder()
                .withConfiguration(new AddressingConfigurationBuilder()
                        .withData(new FileDataSource(Paths.get(dataPath)))
                        .withResources(Paths.get(resourcesPath))
                        .build())
                .build();
        defaultPreferences = new PreferencesBuilder()
                .withReturnAllInfo(true)
                .withFactoryDescription(new FactoryDescriptionBuilder()
                        .withLabel("ggs")
                        .build())
                .build();
    }

    public Response geocode(JsonNode input, JsonNode preferencesJson) {
        try {
            Preferences preferences;
            if(preferencesJson != null) {
                preferences = mapper.treeToValue(preferencesJson, Preferences.class);
            } else {
                preferences = defaultPreferences;
            }

            RequestAddress address = mapper.treeToValue(input, RequestAddress.class);
            log.info("Processing Address [{}] using geocoder.geocode", address.getAddressLines());
            Response response = addressing.geocode(address, preferences);
            log.info("Address successfully geocoded. Formatted address is: {}", response.getResults().get(0).getAddress().getFormattedAddress());
            return response;
        } catch (AddressingException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Response verify(JsonNode input, JsonNode preferencesJson) {
        try {
            Preferences preferences;
            if(preferencesJson != null) {
                preferences = mapper.treeToValue(preferencesJson, Preferences.class);
            } else {
                preferences = defaultPreferences;
            }
            RequestAddress address = mapper.treeToValue(input, RequestAddress.class);
            log.info("Processing Address [{}] using geocoder.verify", address.getAddressLines());
            Response response = addressing.verify(address, preferences);
            log.info("Address successfully verified. Formatted address is: {}", response.getResults().get(0).getAddress().getFormattedAddress());
            return response;
        } catch (AddressingException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
