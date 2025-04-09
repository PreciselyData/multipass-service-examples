package com.precisely.addressing.multipass.demo.custom.executor;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.precisely.addressing.multipass.demo.custom.AddressingEngine;
import com.precisely.addressing.multipass.demo.custom.operator.CustomGeocodeOperator;
import com.precisely.addressing.multipass.sdk.engine.executor.SimpleExecutor;
import com.precisely.addressing.multipass.sdk.model.operator.SimpleOperator;
import com.precisely.addressing.v1.model.Response;

import static com.precisely.addressing.multipass.sdk.util.JsonUtilities.mapper;

public class CustomGeocodeExecutor implements SimpleExecutor {
    private static final CustomGeocodeExecutor instance = instantiate();

    public static CustomGeocodeExecutor getInstance() {
        return instance;
    }

    private static CustomGeocodeExecutor instantiate() {
        return new CustomGeocodeExecutor();
    }

    private CustomGeocodeExecutor() {
    }

    @Override
    public ObjectNode execute(SimpleOperator operator, ObjectNode global) {
        if (operator instanceof CustomGeocodeOperator) {
            CustomGeocodeOperator cgo = (CustomGeocodeOperator) operator;
            String inputKey = cgo.getInputKey();
            String[] inputKeyTree = inputKey.split("\\.");

            JsonNode inputData = extractFromJson(global, inputKeyTree, 0);
            Response data = AddressingEngine.getInstance().geocode(inputData, cgo.getPreferences());
            global.set(cgo.getOutputKey(), mapper.valueToTree(data));
            return global;
        } else {
            throw new RuntimeException("Unsupported input " + operator + "in " + this.getClass().getSimpleName());
        }
    }

    private JsonNode extractFromJson(JsonNode jsonNode, String[] tree, int counter) {
        if(tree.length >= counter + 1) {
            return extractFromJson(jsonNode.get(tree[counter]), tree, counter + 1);
        } else {
            return jsonNode;
        }
    }

}
