package com.labs.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.labs.ORM.Semantics;
import com.labs.Service.SemanticsService;

@Controller
public class SemanticsController {

    @Autowired
    private SemanticsService semanticsService;

    @GetMapping("/semantics")
    public String showSemantics(Model model) {
        List<Semantics> semanticsList = semanticsService.getAllSemantics();
        model.addAttribute("semanticsList", semanticsList);
        return "semantics";
    }

    @GetMapping("/editSemantics")
    public String showEditSemantics(@RequestParam("id") Long semanticId, Model model) {
        Semantics semantics = semanticsService.findSemanticsById(semanticId);
        List<Semantics> allSemantics = semanticsService.getAllSemantics();

        model.addAttribute("semantics", semantics);
        model.addAttribute("allSemantics", allSemantics);

        return "editSemantics";
    }

    @PostMapping("/addSemantics")
    public String addSemantics(@RequestParam("translation") String translation,
                               @RequestParam("explanation") String explanation) {
        Semantics newSemantics = new Semantics();
        newSemantics.setTranslation(translation);
        newSemantics.setExplanation(explanation);
        semanticsService.addSemantics(newSemantics);

        return "redirect:/semantics";
    }

    @GetMapping("/removeSemantics")
    public String removeSemantics(@RequestParam("id") Long semanticsId, Model model) {
        semanticsService.removeSemantics(semanticsId);
        model.addAttribute("actionNameInf", "Remove");
        model.addAttribute("actionNamePast", "removed");
        model.addAttribute("name", "Semantics");
        model.addAttribute("returnLink", "semantics");
        return "actionSuccess";
    }

    @PostMapping("/updateSemantics")
    public String updateSemantics(@RequestParam("semanticId") Long semanticId,
                                  @RequestParam("translation") String translation,
                                  @RequestParam("explanation") String explanation, Model model) {
        Semantics updatedSemantics = semanticsService.findSemanticsById(semanticId);
        updatedSemantics.setTranslation(translation);
        updatedSemantics.setExplanation(explanation);
        semanticsService.updateSemantics(updatedSemantics);

        model.addAttribute("actionNameInf", "Update");
        model.addAttribute("actionNamePast", "updated");
        model.addAttribute("name", "Semantics");
        model.addAttribute("returnLink", "semantics");

        return "actionSuccess";
    }
}

