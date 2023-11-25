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

@WebServlet("/editSemantics")
public class EditSemanticsServlet extends HttpServlet {
    @EJB
    private SemanticsBean semanticsBean;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Long semanticId = Long.parseLong(request.getParameter("id"));
        Semantics semantics = semanticsBean.findSemanticsById(semanticId);
        List<Semantics> allSemantics = semanticsBean.getAllSemantics();

        request.setAttribute("semantics", semantics);
        request.setAttribute("allSemantics", allSemantics);

        RequestDispatcher dispatcher = request.getRequestDispatcher("/editSemantics.jsp");
        dispatcher.forward(request, response);
    }
}
