package org.chorusbdd.web.controller;

import com.fasterxml.jackson.annotation.JsonView;
import org.chorusbdd.structure.pakage.Pakage;
import org.chorusbdd.structure.pakage.Pakages;
import org.chorusbdd.web.view.BinaryResultView;
import org.chorusbdd.web.view.structure.FeatureView;
import org.chorusbdd.web.view.structure.PakageView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

import static org.apache.commons.lang3.Validate.notNull;
import static org.chorusbdd.web.controller.Controllers.redirect;
import static org.chorusbdd.web.controller.ViewMapper.asPakageView;

@RestController
@RequestMapping("/resource")
public class PakageController {

    interface PackagesView extends PakageView.FullDetailView, FeatureView.SummaryView {}

    private final Pakages pakages;

    @Autowired
    public PakageController(final Pakages pakages) {
        this.pakages = notNull(pakages);
    }

    // -------------------------------------------------------- Control Methods

    @RequestMapping(value = "/packages", method = RequestMethod.GET)
    @JsonView(PackagesView.class)
    public PakageView findPakagesRoot() {
        final Pakage pakage = pakages.repository().findRoot();
        if (pakage == null) {
            throw new RuntimeException("test root pakage not found");
        }
        return asPakageView(pakage);
    }

    @RequestMapping(value =  "/packages/{pakageId:.*}", method = RequestMethod.GET)
    @JsonView(PackagesView.class)
    public PakageView findPakage(@PathVariable final String pakageId) {
        final Pakage pakage = pakages.repository().findById(pakageId);
        if (pakage == null) {
            throw new RuntimeException("package not found");
        }
        return asPakageView(pakages.repository().findById(pakageId));
    }

    @RequestMapping(value = "/packages/{pakageId:.*}", method = RequestMethod.PUT)
    public void createPakage(final HttpServletResponse response,
                            @PathVariable final String pakageId) {
        pakages.commands().apply(pakages.events().store(pakageId));
        redirect(response, pakageId);
    }

    @RequestMapping(value = "/move-package", method = RequestMethod.PUT)
    public void movePakage(final HttpServletResponse response,
                           @RequestParam(value = "target",   required = true) String target,
                           @RequestParam(value = "destination",   required = true) String destination) {
        pakages.commands().apply(pakages.events().move(target, destination));
        redirect(response, destination);
    }

    @RequestMapping(value = "/packages/{pakageId:.*}", method = RequestMethod.DELETE)
    public BinaryResultView deletePakage(@PathVariable final String pakageId) {
        final BinaryResultView resultView = new BinaryResultView();
        try {
            pakages.commands().apply(pakages.events().delete(pakageId));
            resultView.setSucceeded(true);
        } catch (Exception e) {
            resultView.setSucceeded(false);
            resultView.setMessage(e);
        }
        return resultView;
    }
}
