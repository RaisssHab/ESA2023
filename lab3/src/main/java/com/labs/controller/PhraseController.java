package com.labs.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.labs.helper.Helper;
import com.labs.helper.PhraseList;
import com.labs.orm.Phrase;
import com.labs.request.PhraseRequest;
import com.labs.service.PhraseService;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;


@RestController
@RequestMapping(value="/phrases", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.TEXT_HTML_VALUE})
public class PhraseController {
    @Autowired
    private PhraseService phraseService;

    @GetMapping
    public ResponseEntity<Object> showPhrases(@RequestHeader("Accept") String acceptHeader) {
        List<Phrase> allPhrases = phraseService.getAllPhrases();

        PhraseList phrasesList = new PhraseList();
        phrasesList.setPhrase(allPhrases);

        return Helper.response(phrasesList, acceptHeader, "/phrases.xslt", PhraseList.class);
    }

    @PostMapping
    public ResponseEntity<Object> addPhrase(@RequestBody PhraseRequest addPhrase, @RequestHeader("Accept") String acceptHeader) {
        Phrase newPhrase = new Phrase();
        newPhrase.setPhrase(addPhrase.getPhrase());
        phraseService.addPhrases(newPhrase);
        
        return Helper.response(newPhrase, acceptHeader, "/phrases.xslt", Phrase.class);
    }

    @DeleteMapping
    public ResponseEntity<Object> removePhrases(@RequestParam("id") Long phraseId, @RequestHeader("Accept") String acceptHeader) {
        Phrase removedPhrase = phraseService.findPhrasesById(phraseId);
        phraseService.removePhrases(phraseId);
        
        return Helper.response(removedPhrase, acceptHeader, "/phrases.xslt", Phrase.class);
    }

    @PutMapping
    public ResponseEntity<Object> updatePhrases(@RequestBody PhraseRequest newPhrase, @RequestHeader("Accept") String acceptHeader) {
        Phrase updatedPhrase = phraseService.findPhrasesById(newPhrase.getPhraseId());
        updatedPhrase.setPhrase(newPhrase.getPhrase());
        phraseService.updatePhrases(updatedPhrase);
        
        return Helper.response(updatedPhrase, acceptHeader, "/phrases.xslt", Phrase.class);
    }
}
