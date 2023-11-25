package com.labs.Servlets;

import com.labs.Beans.PhraseBean;

import javax.ejb.EJB;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/removePhrases")
public class RemovePhrasesServlet extends HttpServlet {
    @EJB
    private PhraseBean phraseBean;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Long phraseId = Long.parseLong(request.getParameter("id"));
        phraseBean.removePhrases(phraseId);

        request.setAttribute("actionNameInf", "Remove");
        request.setAttribute("actionNamePast", "removed");
        request.setAttribute("name", "Phrase");
        request.setAttribute("returnLink", "phrases");

        RequestDispatcher dispatcher = request.getRequestDispatcher("/actionSuccess.jsp");
        dispatcher.forward(request, response);
    }
}
