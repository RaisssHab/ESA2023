package com.labs.Servlets;

import com.labs.Beans.LexiconBean;
import com.labs.Beans.PhraseBean;
import com.labs.Beans.SemanticsBean;
import com.labs.ORM.Lexicon;

import javax.ejb.EJB;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/updateLexicon")
public class UpdateLexiconServlet extends HttpServlet {
    @EJB
    private LexiconBean lexiconBean;

    @EJB
    private SemanticsBean semanticsBean;

    @EJB 
    private PhraseBean phraseBean;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Long lexiconId = Long.parseLong(request.getParameter("lexiconId"));
        Lexicon updatedLexicon = lexiconBean.findLexiconById(lexiconId);

        updatedLexicon.setText(request.getParameter("text"));
        updatedLexicon.setSemantics(semanticsBean.findSemanticsById(Long.parseLong(request.getParameter("semanticList"))));
        updatedLexicon.setPhrase(phraseBean.findPhrasesById(Long.parseLong(request.getParameter("phraseList"))));

        lexiconBean.updateLexicon(updatedLexicon);

        request.setAttribute("actionNameInf", "Update");
        request.setAttribute("actionNamePast", "updated");
        request.setAttribute("name", "Lexicon");
        request.setAttribute("returnLink", "lexicon");

        RequestDispatcher dispatcher = request.getRequestDispatcher("/actionSuccess.jsp");
        dispatcher.forward(request, response);
    }
}