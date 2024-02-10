package com.labs.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.labs.helper.Helper;
import com.labs.helper.LexiconList;
import com.labs.helper.LexiconMapper;
import com.labs.orm.Lexicon;
import com.labs.request.LexiconRequest;
import com.labs.service.LexiconService;
import com.labs.service.PhraseService;
import com.labs.service.SemanticsService;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping(value = "/lexicon", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.TEXT_HTML_VALUE})
public class LexiconController {

    @Autowired
    private LexiconService lexiconService;

    @Autowired
    private SemanticsService semanticsService;

    @Autowired 
    private PhraseService phrasesService;

    @GetMapping
    public ResponseEntity<Object> showLexicon(@RequestHeader("Accept") String acceptHeader) {
        List<Lexicon> allLexicons = lexiconService.getAllLexicons();

        LexiconList lexiconList = new LexiconList();
        lexiconList.setLexicon(allLexicons);

        return Helper.response(lexiconList, acceptHeader, "/lexicon.xslt", LexiconList.class);
    }

    @PostMapping
    public ResponseEntity<Object> addLexicon(@RequestBody LexiconRequest addLexicon, @RequestHeader("Accept") String acceptHeader) {
        Lexicon newLexicon = new Lexicon();
        newLexicon.setText(addLexicon.getText());
        newLexicon.setSemantics(semanticsService.findSemanticsById(addLexicon.getSemanticId()));
        newLexicon.setPhrase(phrasesService.findPhrasesById(addLexicon.getPhraseId()));

        lexiconService.createLexicon(newLexicon);

        return Helper.response(newLexicon, acceptHeader, "/lexicon.xslt", Lexicon.class);
    }

    @DeleteMapping
    public ResponseEntity<Object> removeLexicon(@RequestParam("id") Long lexiconId, @RequestHeader("Accept") String acceptHeader) {
        Lexicon removedLexicon = lexiconService.getLexiconById(lexiconId);
        lexiconService.deleteLexicon(lexiconId);

        return Helper.response(removedLexicon, acceptHeader, "/lexicon.xslt", Lexicon.class);
    }

    @PutMapping
    public ResponseEntity<Object> updateLexicon(@RequestBody LexiconRequest newLexicon, @RequestHeader("Accept") String acceptHeader) {
        Lexicon updatedLexicon = lexiconService.getLexiconById(newLexicon.getLexiconId());
        Map<String, Object> oldLexiconMap = LexiconMapper.map(updatedLexicon, updatedLexicon.getPhrase(), updatedLexicon.getSemantics());
        updatedLexicon.setText(newLexicon.getText());
        updatedLexicon.setSemantics(semanticsService.findSemanticsById(newLexicon.getSemanticId()));
        updatedLexicon.setPhrase(phrasesService.findPhrasesById(newLexicon.getPhraseId()));

        lexiconService.updateLexicon(updatedLexicon, oldLexiconMap);

        return Helper.response(updatedLexicon, acceptHeader, "/lexicon.xslt", Lexicon.class);
    }
}