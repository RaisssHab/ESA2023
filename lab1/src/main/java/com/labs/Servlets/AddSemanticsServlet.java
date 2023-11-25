package com.labs.Servlets;

import com.labs.Beans.SemanticsBean;
import com.labs.ORM.Semantics;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/addSemantics")
public class AddSemanticsServlet extends HttpServlet {
    @EJB
    private SemanticsBean semanticsBean;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Semantics newSemantics = new Semantics();

        newSemantics.setTranslation(request.getParameter("translation"));
        newSemantics.setExplanation(request.getParameter("explanation"));

        semanticsBean.addSemantics(newSemantics);

        response.sendRedirect(request.getContextPath() + "/semantics");
    }
}
