package com.labs.Servlets;

import com.labs.Beans.LexiconBean;

import javax.ejb.EJB;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/removeLexicon")
public class RemoveLexiconServlet extends HttpServlet {
    @EJB
    private LexiconBean lexiconBean;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Long lexiconId = Long.parseLong(request.getParameter("id"));
        lexiconBean.removeLexicon(lexiconId);

        request.setAttribute("actionNameInf", "Remove");
        request.setAttribute("actionNamePast", "removed");
        request.setAttribute("name", "Lexicon");
        request.setAttribute("returnLink", "lexicon");

        RequestDispatcher dispatcher = request.getRequestDispatcher("/actionSuccess.jsp");
        dispatcher.forward(request, response);
    }
}
