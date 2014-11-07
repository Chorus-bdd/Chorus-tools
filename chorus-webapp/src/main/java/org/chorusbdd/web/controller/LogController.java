package org.chorusbdd.web.controller;

import org.chorusbdd.history.Svc;
import org.chorusbdd.web.view.ModificationView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

import static org.apache.commons.lang3.Validate.notNull;


@RestController
@RequestMapping("/resource")
public class LogController {

    private Svc svc;

    @Autowired
    public LogController(final Svc svc) {
        this.svc = notNull(svc);
    }

    // -------------------------------------------------------- Control Methods

    @RequestMapping(value = "/log", method = RequestMethod.GET)
    public List<ModificationView> log() {
        return svc.log().map(ViewMapper::asModificationView).collect(Collectors.toList());
    }


    //@RequestMapping(value = "/log/features/{featureId:.*}", method = RequestMethod.GET)
    //public List<ModificationView> featureLog() {
    //    return features.repository().findLogById(featureId) .map(ViewMapper::asModificationView).collect(Collectors.toList());
    //}
}