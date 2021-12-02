package com.cirdles;

import com.google.gson.Gson;
import org.cirdles.squid.Squid3API;
import org.cirdles.squid.constants.Squid3Constants;
import org.cirdles.squid.Squid3Ink;
import org.cirdles.squid.exceptions.SquidException;
import org.cirdles.squid.projects.Squid3ProjectBasicAPI;
import org.cirdles.squid.tasks.TaskInterface;
import org.cirdles.squid.tasks.expressions.Expression;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;
import static org.cirdles.squid.tasks.expressions.Expression.makeExpressionForAudit;
import static org.cirdles.squid.tasks.expressions.builtinExpressions.BuiltInExpressionsDataDictionary.*;

/**
 * Servlet implementation class FileUploadServlet
 */

@WebServlet(name = "CurrentTaskStringsServlet", urlPatterns = {"/CurrentTaskStringsServlet"})
@MultipartConfig(
        fileSizeThreshold = 1024 * 1024 * 1, // MB
        maxFileSize = 1024 * 1024 * 10, // 10 MB
        maxRequestSize = 1024 * 1024 * 100 // 100 MB
)


public class CurrentTaskStringsServlet extends HttpServlet {
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
            try {
                task.setupSquidSessionSpecsAndReduceAndReport(false);
            } catch (SquidException squidException) {
                System.out.println(squidException);
            }
            boolean uPicked = task.getParentNuclide().equals("238");
            boolean directPicked = task.isDirectAltPD();;
            boolean perm1 = uPicked && !directPicked;
            boolean perm2 = uPicked && directPicked;
            boolean perm3 = !uPicked && !directPicked;
            boolean perm4 = !uPicked && directPicked;
            ArrayList<String> outputArr = new ArrayList<>();

            outputArr.add(task.getName());
            outputArr.add(task.getDescription());
            outputArr.add(task.getAuthorName());
            outputArr.add(task.getLabName());
            outputArr.add(task.getProvenance());
            outputArr.add(task.getParentNuclide());
            outputArr.add(task.isDirectAltPD() ? "direct":"indirect");

            Expression UTh_U = task.getExpressionByName(UNCOR206PB238U_CALIB_CONST);
            String UTh_U_ExpressionString = (UTh_U == null) ? UNCOR206PB238U_CALIB_CONST_DEFAULT_EXPRESSION : UTh_U.getExcelExpressionString();

            Expression UTh_Th = task.getExpressionByName(UNCOR208PB232TH_CALIB_CONST);
            String UTh_Th_ExpressionString = (UTh_Th == null) ? UNCOR208PB232TH_CALIB_CONST_DEFAULT_EXPRESSION : UTh_Th.getExcelExpressionString();

            Expression thU = task.getExpressionByName(TH_U_EXP_RM);
            String thU_ExpressionString = (thU == null) ? TH_U_EXP_DEFAULT_EXPRESSION : thU.getExcelExpressionString();

            Expression parentPPM = task.getExpressionByName(PARENT_ELEMENT_CONC_CONST);
            String parentPPM_ExpressionString = (parentPPM == null) ? PARENT_ELEMENT_CONC_CONST_DEFAULT_EXPRESSION : parentPPM.getExcelExpressionString();

            outputArr.add((perm1 || perm2 || perm4) ? UTh_U_ExpressionString : "Not Used");
            outputArr.add((perm2 || perm3 || perm4) ? UTh_Th_ExpressionString : "Not Used");
            outputArr.add((perm1 || perm3) ? thU_ExpressionString : "Not Used");
            outputArr.add(parentPPM_ExpressionString);

            outputArr.add(Boolean.toString(makeExpression(UNCOR206PB238U_CALIB_CONST, UTh_U_ExpressionString).amHealthy()));
            outputArr.add(Boolean.toString(makeExpression(UNCOR208PB232TH_CALIB_CONST, UTh_Th_ExpressionString).amHealthy()));
            outputArr.add(Boolean.toString(makeExpression(TH_U_EXP_DEFAULT, thU_ExpressionString).amHealthy()));
            outputArr.add(Boolean.toString(makeExpression(PARENT_ELEMENT_CONC_CONST, parentPPM_ExpressionString).amHealthy()));
            System.out.println(makeExpression(UNCOR206PB238U_CALIB_CONST, UTh_U_ExpressionString).amHealthy());
            System.out.println(makeExpression(UNCOR208PB232TH_CALIB_CONST, UTh_Th_ExpressionString).amHealthy());
            System.out.println(makeExpression(TH_U_EXP_DEFAULT, thU_ExpressionString).amHealthy());
            System.out.println(makeExpression(PARENT_ELEMENT_CONC_CONST, parentPPM_ExpressionString).amHealthy());
            outputArr.add(task.printTaskAudit());
            Gson packager = new Gson();
            response.getWriter().println(packager.toJson(Arrays.toString(outputArr.toArray())));
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


