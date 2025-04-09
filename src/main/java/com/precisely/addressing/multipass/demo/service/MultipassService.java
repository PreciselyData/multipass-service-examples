package com.precisely.addressing.multipass.demo.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.precisely.addressing.multipass.api.MultipassContext;
import com.precisely.addressing.v1.model.RequestAddress;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MultipassService {
    final MultipassContext multipassContext;

    public MultipassService() {
        this.multipassContext = MultipassContext.builder().addFlowCache().create();
    }

    public List<JsonNode> enhancedGeocode(List<RequestAddress> addresses, String flowId) {
        if (addresses == null)
            throw new RuntimeException("Address can't be null");

        return addresses.stream()
                .map(address -> processGenericResponse(address, flowId))
                .toList();
    }

    private JsonNode processGenericResponse(RequestAddress address, String flowId) {
        ObjectMapper mapper = new ObjectMapper();

        ObjectNode globals = mapper.convertValue(address, ObjectNode.class);

        return multipassContext.execute(flowId, globals);
    }
}
