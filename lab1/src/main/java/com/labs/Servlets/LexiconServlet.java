package com.labs.Servlets;

import java.io.IOException;
import java.util.List;

import javax.ejb.EJB;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.labs.Beans.LexiconBean;
import com.labs.Beans.PhraseBean;
import com.labs.Beans.SemanticsBean;
import com.labs.ORM.Lexicon;
import com.labs.ORM.Phrase;
import com.labs.ORM.Semantics;

@WebServlet("/lexicon")
public class LexiconServlet extends HttpServlet {
    @EJB
    private LexiconBean lexiconBean;

    @EJB
    private SemanticsBean semanticsBean;

    @EJB
    private PhraseBean phrasesBean;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Lexicon> lexiconList = lexiconBean.getAllLexicon();
        
        List<Semantics> allSemantics = semanticsBean.getAllSemantics();
        List<Phrase> allPhrases = phrasesBean.getAllPhrases();

        request.setAttribute("lexiconList", lexiconList);
        request.setAttribute("allSemantics", allSemantics);
        request.setAttribute("allPhrases", allPhrases);
        
        RequestDispatcher dispatcher = request.getRequestDispatcher("/lexicon.jsp");
        dispatcher.forward(request, response);
    }
}
