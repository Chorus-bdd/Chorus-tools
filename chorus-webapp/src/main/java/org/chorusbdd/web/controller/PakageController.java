package org.chorusbdd.web.controller;

import com.fasterxml.jackson.annotation.JsonView;
import org.chorusbdd.structure.pakage.Pakage;
import org.chorusbdd.structure.pakage.Pakages;
import org.chorusbdd.web.view.ErrorEntity;
import org.chorusbdd.web.view.structure.FeatureView;
import org.chorusbdd.web.view.structure.PakageView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

import static javax.servlet.http.HttpServletResponse.SC_NOT_FOUND;
import static org.apache.commons.lang3.Validate.notNull;
import static org.chorusbdd.web.controller.Controllers.pakageLink;
import static org.chorusbdd.web.controller.Controllers.redirect;
import static org.chorusbdd.web.controller.Controllers.resourceNotFound;
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
    public Object findPakagesRoot(final HttpServletResponse response) {
        final Pakage pakage = pakages.repository().findRoot();
        if (pakage == null) {
            return resourceNotFound(response, "Root package not found");
        }
        return asPakageView(pakage);
    }

    @RequestMapping(value =  "/packages/{pakageId}", method = RequestMethod.GET)
    @JsonView(PackagesView.class)
    public Object findPakage(final HttpServletResponse response,
                                 @PathVariable final String pakageId) {
        final Pakage pakage = pakages.repository().findById(pakageId);
        if (pakage == null) {
            return resourceNotFound(response, "Package with id '" + pakageId + "' not found");
        }
        return asPakageView(pakages.repository().findById(pakageId));
    }

    @RequestMapping(value = "/packages/{pakageId}", method = RequestMethod.PUT)
    public void createPakage(final HttpServletResponse response,
                            @PathVariable final String pakageId) {
        pakages.commands().apply(pakages.events().store(pakageId));
        Controllers.resourceCreated(response, pakageId, pakageLink(pakageId));
    }

    @RequestMapping(value = "/packages/{targetPackageId}/move", method = RequestMethod.PUT)
    public PakageView movePakage(@PathVariable final String targetPackageId,
                                   @RequestParam(value = "to",   required = true) String destPackageId) {
        pakages.commands().apply(pakages.events().move(targetPackageId, destPackageId));
        return asPakageView(pakages.repository().findById(destPackageId));
    }

    @RequestMapping(value = "/packages/{pakageId}", method = RequestMethod.DELETE)
    public ErrorEntity deletePakage(final HttpServletResponse response,
                                    @PathVariable final String pakageId) {
        final ErrorEntity resultView = new ErrorEntity();
        try {
            pakages.commands().apply(pakages.events().delete(pakageId));
            response.setStatus(HttpServletResponse.SC_NO_CONTENT);
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resultView.setMessage(e);
        }
        return resultView;
    }
}
