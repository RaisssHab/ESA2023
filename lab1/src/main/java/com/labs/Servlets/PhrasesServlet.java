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

import com.labs.Beans.PhraseBean;
import com.labs.ORM.Phrase;

@WebServlet("/phrases")
public class PhrasesServlet extends HttpServlet {
    @EJB
    private PhraseBean phrasesBean;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Phrase> phrasesList = phrasesBean.getAllPhrases();
        request.setAttribute("phrasesList", phrasesList);
        RequestDispatcher dispatcher = request.getRequestDispatcher("/phrases.jsp");
        dispatcher.forward(request, response);
    }
}
