package com.labs.Servlets;

import com.labs.Beans.LexiconBean;
import com.labs.Beans.PhraseBean;
import com.labs.Beans.SemanticsBean;
import com.labs.ORM.Lexicon;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/addLexicon")
public class AddLexiconServlet extends HttpServlet {
    @EJB
    private LexiconBean lexiconBean;

    @EJB
    private SemanticsBean semanticsBean;

    @EJB 
    private PhraseBean phraseBean;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Lexicon newLexicon = new Lexicon();

        newLexicon.setText(request.getParameter("text"));
        newLexicon.setSemantics(semanticsBean.findSemanticsById(Long.parseLong(request.getParameter("semanticList"))));
        newLexicon.setPhrase(phraseBean.findPhrasesById(Long.parseLong(request.getParameter("phraseList"))));

        lexiconBean.addLexicon(newLexicon);

        response.sendRedirect(request.getContextPath() + "/lexicon");
    }
}