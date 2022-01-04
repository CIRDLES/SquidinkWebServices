package com.cirdles;

import com.google.gson.Gson;
import org.cirdles.squid.Squid3API;

import org.cirdles.squid.tasks.TaskInterface;
import org.cirdles.squid.tasks.expressions.Expression;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;

import java.util.stream.Collectors;
import static org.cirdles.squid.tasks.expressions.Expression.makeExpressionForAudit;


/**
 * Servlet implementation class FileUploadServlet
 */

@WebServlet(name = "CurrentTaskAudit", urlPatterns = {"/curtaskaudit"})
@MultipartConfig(
        fileSizeThreshold = 1024 * 1024 * 1, // MB
        maxFileSize = 1024 * 1024 * 10, // 10 MB
        maxRequestSize = 1024 * 1024 * 100 // 100 MB
)


public class CurrentTaskAudit extends HttpServlet {
    private TaskInterface task;
    private static final long serialVersionUID = 1L;
    private boolean perm2;

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request  servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException      if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            String body = request.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
            Squid3API squid = (Squid3API) this.getServletConfig().getServletContext().getAttribute(body);
            task = squid.getSquid3Project().getTask();
            Gson packager = new Gson();
            response.getWriter().println(packager.toJson(task.printTaskAudit()));
        } catch (Exception e) {
            response.getWriter().print(e);
            e.printStackTrace();
            System.out.print(e);
        }
    }

    private Expression makeExpression(String expressionName, final String expressionString) {
        return makeExpressionForAudit(expressionName, expressionString, task.getNamedExpressionsMap());
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Click Event Servlet";
    }// </editor-fold>
}


