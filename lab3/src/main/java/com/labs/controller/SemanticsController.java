package com.labs.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.labs.helper.Helper;
import com.labs.helper.SemanticsList;
import com.labs.orm.Semantics;
import com.labs.request.SemanticsRequest;
import com.labs.service.SemanticsService;

@RestController
@RequestMapping(value = "/semantics", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.TEXT_HTML_VALUE})
public class SemanticsController {

    @Autowired
    private SemanticsService semanticsService;

    @GetMapping
    public ResponseEntity<Object> showSemantics(@RequestHeader("Accept") String acceptHeader) {
        List<Semantics> allSemantics = semanticsService.getAllSemantics();

        SemanticsList semanticsList = new SemanticsList();
        semanticsList.setSemantics(allSemantics);

        return Helper.response(semanticsList, acceptHeader, "/semantics.xslt", SemanticsList.class);
    }

    @PostMapping
    public ResponseEntity<Object> addSemantics(@RequestBody SemanticsRequest addSemantics, @RequestHeader("Accept") String acceptHeader) {
        Semantics newSemantics = new Semantics();
        newSemantics.setTranslation(addSemantics.getTranslation());
        newSemantics.setExplanation(addSemantics.getExplanation());
        semanticsService.addSemantics(newSemantics);

        return Helper.response(newSemantics, acceptHeader, "/semantics.xslt", Semantics.class);
    }

    @DeleteMapping
    public ResponseEntity<Object> removeSemantics(@RequestParam("id") Long semanticsId, @RequestHeader("Accept") String acceptHeader) {
        Semantics removedSemantics = semanticsService.findSemanticsById(semanticsId);
        semanticsService.removeSemantics(semanticsId);

        return Helper.response(removedSemantics, acceptHeader, "/semantics.xslt", Semantics.class);
    }

    @PutMapping
    public ResponseEntity<Object> updateSemantics(@RequestBody SemanticsRequest newSemantics, @RequestHeader("Accept") String acceptHeader) {

        Semantics updatedSemantics = semanticsService.findSemanticsById(newSemantics.getSemanticId());
        updatedSemantics.setTranslation(newSemantics.getTranslation());
        updatedSemantics.setExplanation(newSemantics.getExplanation());
        semanticsService.updateSemantics(updatedSemantics);

        return Helper.response(updatedSemantics, acceptHeader, "/semantics.xslt", Semantics.class);
    }
}

