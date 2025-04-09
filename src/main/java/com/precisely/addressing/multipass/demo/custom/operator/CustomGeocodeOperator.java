package com.precisely.addressing.multipass.demo.custom.operator;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.precisely.addressing.multipass.demo.custom.executor.CustomGeocodeExecutor;
import com.precisely.addressing.multipass.sdk.engine.executor.SimpleExecutor;
import com.precisely.addressing.multipass.sdk.model.operator.SimpleOperator;
import com.precisely.addressing.multipass.sdk.registration.AsOperator;
import com.precisely.addressing.multipass.sdk.registration.NotNullData;

@JsonDeserialize(as = CustomGeocodeOperator.class)
@AsOperator(name = "Geocode")
public class CustomGeocodeOperator implements SimpleOperator {

    @NotNullData
    private String id;
    private String description;
    @NotNullData
    private String nextNode;
    @NotNullData
    private String inputKey;
    @NotNullData
    private String outputKey;
    private JsonNode preferences;

    @Override
    public SimpleExecutor getExecutor() {
        return CustomGeocodeExecutor.getInstance();
    }

    @Override
    public String getNextNode() {
        return nextNode;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getDescription() {
        return description;
    }

    public String getInputKey() {
        return inputKey;
    }

    public String getOutputKey() {
        return outputKey;
    }

    public JsonNode getPreferences() {
        return preferences;
    }
}
