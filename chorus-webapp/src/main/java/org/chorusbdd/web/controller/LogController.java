package org.chorusbdd.web.controller;

import org.chorusbdd.history.Svc;
import org.chorusbdd.structure.FileSystemDatabase;
import org.chorusbdd.web.view.RevisionView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static java.util.stream.Collectors.toList;
import static org.apache.commons.lang3.Validate.notNull;


@RestController
@RequestMapping("/resource")
public class LogController {

    private Svc svc;
    private FileSystemDatabase paths;

    @Autowired
    public LogController(final Svc svc, final FileSystemDatabase paths) {
        this.svc = notNull(svc);
        this.paths = notNull(paths);
    }

    // -------------------------------------------------------- Control Methods

    @RequestMapping(value = "/log", method = RequestMethod.GET)
    public List<RevisionView> log() {
        return svc.log().map(ViewMapper::asRevisionView).collect(toList());
    }

    /** Returns a list of affected id's. */
    @RequestMapping(value = "/log/{revisionId}/changeset", method = RequestMethod.GET)
    public List<String> changeset(@PathVariable final String revisionId) {
        return svc.changesetForRevision(revisionId).stream()
                .map(paths::pathToId).collect(toList());
    }
}