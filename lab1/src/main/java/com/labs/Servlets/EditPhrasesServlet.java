package com.labs.Servlets;

import java.io.IOException;

import javax.ejb.EJB;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.labs.Beans.PhraseBean;
import com.labs.ORM.Phrase;

@WebServlet("/editPhrases")
public class EditPhrasesServlet extends HttpServlet {
    @EJB
    private PhraseBean phrasesBean;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Long phraseId = Long.parseLong(request.getParameter("id"));
        Phrase phrases = phrasesBean.findPhrasesById(phraseId);

        request.setAttribute("phrases", phrases);

        RequestDispatcher dispatcher = request.getRequestDispatcher("/editPhrases.jsp");
        dispatcher.forward(request, response);
    }
}
