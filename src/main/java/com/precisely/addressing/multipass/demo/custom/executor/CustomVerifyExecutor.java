package com.precisely.addressing.multipass.demo.custom.executor;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.precisely.addressing.multipass.demo.custom.operator.CustomVerifyOperator;
import com.precisely.addressing.multipass.demo.custom.AddressingEngine;
import com.precisely.addressing.multipass.sdk.engine.executor.SimpleExecutor;
import com.precisely.addressing.multipass.sdk.model.operator.SimpleOperator;
import com.precisely.addressing.v1.model.Response;

import static com.precisely.addressing.multipass.sdk.util.JsonUtilities.mapper;

public class CustomVerifyExecutor implements SimpleExecutor {
    private static final CustomVerifyExecutor instance = instantiate();

    public static CustomVerifyExecutor getInstance() {
        return instance;
    }

    private static CustomVerifyExecutor instantiate() {
        return new CustomVerifyExecutor();
    }

    private CustomVerifyExecutor() {
    }

    @Override
    public ObjectNode execute(SimpleOperator operator, ObjectNode global) {
        if (operator instanceof CustomVerifyOperator) {
            CustomVerifyOperator cvo = (CustomVerifyOperator) operator;
            JsonNode inputData = extractFromJson(global, cvo.getInputKey().split("\\."), 0);
            Response data = AddressingEngine.getInstance().verify(inputData, cvo.getPreferences());
            global.set(cvo.getOutputKey(), mapper.valueToTree(data));
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
