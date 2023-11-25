package com.labs.Servlets;

import com.labs.Beans.PhraseBean;
import com.labs.ORM.Phrase;

import javax.ejb.EJB;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/updatePhrases")
public class UpdatePhrasesServlet extends HttpServlet {
    @EJB
    private PhraseBean phraseBean;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Long phraseId = Long.parseLong(request.getParameter("phraseId"));
        Phrase updatedPhrases = phraseBean.findPhrasesById(phraseId);
        updatedPhrases.setPhrase(request.getParameter("phrase"));
        phraseBean.updatePhrases(updatedPhrases);

        request.setAttribute("actionNameInf", "Update");
        request.setAttribute("actionNamePast", "updated");
        request.setAttribute("name", "Phrase");
        request.setAttribute("returnLink", "phrases");

        RequestDispatcher dispatcher = request.getRequestDispatcher("/actionSuccess.jsp");
        dispatcher.forward(request, response);
    }
}
