package com.labs.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.labs.ORM.Phrase;
import com.labs.Service.PhraseService;

@Controller
public class PhraseController {

    @Autowired
    private PhraseService phraseService;

    @GetMapping("/phrases")
    public String showPhrases(Model model) {
        List<Phrase> phrasesList = phraseService.getAllPhrases();
        model.addAttribute("phrasesList", phrasesList);
        return "phrases";
    }

    @GetMapping("/editPhrases")
    public String showEditPhrases(@RequestParam("id") Long phraseId, Model model) {
        Phrase phrases = phraseService.findPhrasesById(phraseId);
        model.addAttribute("phrases", phrases);
        return "editPhrases";
    }

    @PostMapping("/addPhrase")
    public String addPhrase(@RequestParam("phrase") String phrase) {
        Phrase newPhrase = new Phrase();
        newPhrase.setPhrase(phrase);
        phraseService.addPhrases(newPhrase);
        return "redirect:/phrases";
    }

    @GetMapping("/removePhrases")
    public String removePhrases(@RequestParam("id") Long phraseId, Model model) {
        phraseService.removePhrases(phraseId);
        model.addAttribute("actionNameInf", "Remove");
        model.addAttribute("actionNamePast", "removed");
        model.addAttribute("name", "Phrase");
        model.addAttribute("returnLink", "phrases");
        return "actionSuccess";
    }

    @PostMapping("/updatePhrases")
    public String updatePhrases(@RequestParam("phraseId") Long phraseId,
                                @RequestParam("phrase") String phrase, Model model) {
        Phrase updatedPhrases = phraseService.findPhrasesById(phraseId);
        updatedPhrases.setPhrase(phrase);
        phraseService.updatePhrases(updatedPhrases);
        model.addAttribute("actionNameInf", "Update");
        model.addAttribute("actionNamePast", "updated");
        model.addAttribute("name", "Phrase");
        model.addAttribute("returnLink", "phrases");
        return "actionSuccess";
    }
}
