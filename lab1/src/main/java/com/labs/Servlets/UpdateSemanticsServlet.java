package com.labs.Servlets;

import com.labs.Beans.SemanticsBean;
import com.labs.ORM.Semantics;

import javax.ejb.EJB;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/updateSemantics")
public class UpdateSemanticsServlet extends HttpServlet {
    @EJB
    private SemanticsBean semanticsBean;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Long semanticId = Long.parseLong(request.getParameter("semanticId"));
        Semantics updatedSemantics = semanticsBean.findSemanticsById(semanticId);
        updatedSemantics.setTranslation(request.getParameter("translation"));
        updatedSemantics.setExplanation(request.getParameter("explanation"));
        semanticsBean.updateSemantics(updatedSemantics);

        request.setAttribute("actionNameInf", "Update");
        request.setAttribute("actionNamePast", "updated");
        request.setAttribute("name", "Semantics");
        request.setAttribute("returnLink", "semantics");

        RequestDispatcher dispatcher = request.getRequestDispatcher("/actionSuccess.jsp");
        dispatcher.forward(request, response);
    }
}
