package org.chorusbdd.web.controller;

import org.chorusbdd.structure.feature.Features;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import static org.apache.commons.lang3.Validate.notNull;


@RestController
@RequestMapping("/resource/status")
public class StatusController {

    private final Features features;

    @Autowired
    public StatusController(final Features features) {
        this.features = notNull(features);
    }

    // -------------------------------------------------------- Control Methods

    @RequestMapping(value = "", method = RequestMethod.GET)
    public String status() {
        return "OK";
    }
}