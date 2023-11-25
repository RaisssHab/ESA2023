package com.labs.Servlets;

import com.labs.Beans.PhraseBean;
import com.labs.ORM.Phrase;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/addPhrase")
public class AddPhraseServlet extends HttpServlet {
    @EJB
    private PhraseBean phraseBean;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Phrase newPhrase = new Phrase();

        newPhrase.setPhrase(request.getParameter("phrase"));

        phraseBean.addPhrases(newPhrase);

        response.sendRedirect(request.getContextPath() + "/phrases");
    }
}
