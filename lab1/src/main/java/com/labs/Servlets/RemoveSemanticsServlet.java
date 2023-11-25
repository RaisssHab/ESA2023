package com.labs.Servlets;

import com.labs.Beans.SemanticsBean;

import javax.ejb.EJB;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/removeSemantics")
public class RemoveSemanticsServlet extends HttpServlet {
    @EJB
    private SemanticsBean semanticsBean;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Long semanticsId = Long.parseLong(request.getParameter("id"));
        semanticsBean.removeSemantics(semanticsId);

        request.setAttribute("actionNameInf", "Remove");
        request.setAttribute("actionNamePast", "removed");
        request.setAttribute("name", "Semantics");
        request.setAttribute("returnLink", "semantics");

        RequestDispatcher dispatcher = request.getRequestDispatcher("/actionSuccess.jsp");
        dispatcher.forward(request, response);
    }
}
