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

import com.labs.Beans.SemanticsBean;
import com.labs.ORM.Semantics;

@WebServlet("/semantics")
public class SemanticsServlet extends HttpServlet {
    @EJB
    private SemanticsBean semanticsBean;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Semantics> semanticsList = semanticsBean.getAllSemantics();
        request.setAttribute("semanticsList", semanticsList);
        RequestDispatcher dispatcher = request.getRequestDispatcher("/semantics.jsp");
        dispatcher.forward(request, response);
    }
}
