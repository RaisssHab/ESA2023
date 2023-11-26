package com.labs.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.labs.ORM.Lexicon;
import com.labs.ORM.Phrase;
import com.labs.ORM.Semantics;
import com.labs.Service.LexiconService;
import com.labs.Service.PhraseService;
import com.labs.Service.SemanticsService;

@Controller
public class LexiconController {

    @Autowired
    private LexiconService lexiconService;

    @Autowired
    private SemanticsService semanticsService;

    @Autowired
    private PhraseService phrasesService;

    @GetMapping("/")
    public String redirectToLexicon() {
        return "redirect:/lexicon";
    }

    @GetMapping("/lexicon")
    public String showLexicon(Model model) {
        List<Lexicon> lexiconList = lexiconService.getAllLexicons();
        List<Semantics> allSemantics = semanticsService.getAllSemantics();
        List<Phrase> allPhrases = phrasesService.getAllPhrases();

        model.addAttribute("lexiconList", lexiconList);
        model.addAttribute("allSemantics", allSemantics);
        model.addAttribute("allPhrases", allPhrases);

        return "lexicon";
    }

    @GetMapping("/editLexicon")
    public String showEditLexicon(@RequestParam("id") Long lexiconId, Model model) {
        Lexicon lexicon = lexiconService.getLexiconById(lexiconId);
        List<Semantics> allSemantics = semanticsService.getAllSemantics();
        List<Phrase> allPhrases = phrasesService.getAllPhrases();

        model.addAttribute("lexicon", lexicon);
        model.addAttribute("allSemantics", allSemantics);
        model.addAttribute("allPhrases", allPhrases);

        return "editLexicon";
    }

    @PostMapping("/addLexicon")
    public String addLexicon(@RequestParam("text") String text,
                             @RequestParam("semanticList") Long semanticId,
                             @RequestParam("phraseList") Long phraseId) {
        Lexicon newLexicon = new Lexicon();
        newLexicon.setText(text);
        newLexicon.setSemantics(semanticsService.findSemanticsById(semanticId));
        newLexicon.setPhrase(phrasesService.findPhrasesById(phraseId));

        lexiconService.createLexicon(newLexicon);

        return "redirect:/lexicon";
    }

    @GetMapping("/removeLexicon")
    public String removeLexicon(@RequestParam("id") Long lexiconId, Model model) {
        lexiconService.deleteLexicon(lexiconId);

        model.addAttribute("actionNameInf", "Remove");
        model.addAttribute("actionNamePast", "removed");
        model.addAttribute("name", "Lexicon");
        model.addAttribute("returnLink", "lexicon");

        return "actionSuccess";
    }

    @PostMapping("/updateLexicon")
    public String updateLexicon(@RequestParam("lexiconId") Long lexiconId,
                                @RequestParam("text") String text,
                                @RequestParam("semanticList") Long semanticId,
                                @RequestParam("phraseList") Long phraseId, Model model) {

        Lexicon updatedLexicon = lexiconService.getLexiconById(lexiconId);
        updatedLexicon.setText(text);
        updatedLexicon.setSemantics(semanticsService.findSemanticsById(semanticId));
        updatedLexicon.setPhrase(phrasesService.findPhrasesById(phraseId));

        lexiconService.updateLexicon(updatedLexicon);

        model.addAttribute("actionNameInf", "Update");
        model.addAttribute("actionNamePast", "updated");
        model.addAttribute("name", "Lexicon");
        model.addAttribute("returnLink", "lexicon");

        return "actionSuccess";
    }
}
