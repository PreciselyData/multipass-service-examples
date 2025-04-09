package com.precisely.addressing.multipass.demo.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.precisely.addressing.multipass.demo.service.MultipassService;
import com.precisely.addressing.v1.model.RequestAddress;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class MultipassController {

    private final MultipassService multipassService;

    public MultipassController(MultipassService multipassService) {
        this.multipassService = multipassService;
    }

    @GetMapping("/")
    public String index() {
        return "Greetings from Spring Boot!";
    }

    @PostMapping("/multipass/{flowId}")
    public List<JsonNode> geocode(@PathVariable("flowId") String flowId, @RequestBody List<RequestAddress> addresses) {
        return multipassService.enhancedGeocode(addresses, flowId);
    }
}
