package com.cirdles;

import com.google.gson.Gson;
import org.cirdles.squid.Squid3API;
import org.cirdles.squid.constants.Squid3Constants;
import org.cirdles.squid.Squid3Ink;
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
    private static final long serialVersionUID = 1L;

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
            TaskInterface squidTask = squid.getSquid3Project().getTask();
            boolean uPicked = squidTask.getParentNuclide().equals("238");
            boolean directPicked = squidTask.isDirectAltPD();;
            boolean perm1 = uPicked && !directPicked;
            boolean perm2 = uPicked && directPicked;
            boolean perm3 = !uPicked && !directPicked;
            boolean perm4 = !uPicked && directPicked;
            ArrayList<String> outputArr = new ArrayList<>();

            outputArr.add(squidTask.getName());
            outputArr.add(squidTask.getDescription());
            outputArr.add(squidTask.getAuthorName());
            outputArr.add(squidTask.getLabName());
            outputArr.add(squidTask.getProvenance());
            outputArr.add(squidTask.getParentNuclide());
            outputArr.add(squidTask.isDirectAltPD() ? "direct":"indirect");

            String Uth = squidTask.getExpressionByName(UNCOR206PB238U_CALIB_CONST) == null ? UNCOR206PB238U_CALIB_CONST_DEFAULT_EXPRESSION  : squidTask.getExpressionByName(UNCOR206PB238U_CALIB_CONST).getExcelExpressionString();
            String Uth_Th = squidTask.getExpressionByName(UNCOR208PB232TH_CALIB_CONST) == null ? UNCOR208PB232TH_CALIB_CONST_DEFAULT_EXPRESSION  : squidTask.getExpressionByName(UNCOR208PB232TH_CALIB_CONST).getExcelExpressionString();
            String thU = squidTask.getExpressionByName(TH_U_EXP_RM) == null ? TH_U_EXP_DEFAULT_EXPRESSION  : squidTask.getExpressionByName(TH_U_EXP_RM).getExcelExpressionString();
            String parEle = squidTask.getExpressionByName(PARENT_ELEMENT_CONC_CONST) == null ? PARENT_ELEMENT_CONC_CONST_DEFAULT_EXPRESSION  : squidTask.getExpressionByName(PARENT_ELEMENT_CONC_CONST).getExcelExpressionString();
            outputArr.add((perm1 || perm2 || perm4) ? Uth : "Not Used");
            outputArr.add((perm2 || perm3 || perm4) ? Uth_Th : "Not Used");
            outputArr.add((perm1 || perm3) ? thU : "Not Used");
            outputArr.add(parEle);

            outputArr.add(Boolean.toString(makeExpressionForAudit(UNCOR206PB238U_CALIB_CONST, Uth, squidTask.getNamedExpressionsMap()).amHealthy()));
            outputArr.add(Boolean.toString(makeExpressionForAudit(UNCOR208PB232TH_CALIB_CONST, Uth_Th, squidTask.getNamedExpressionsMap()).amHealthy()));
            outputArr.add(Boolean.toString(makeExpressionForAudit(TH_U_EXP_RM, thU, squidTask.getNamedExpressionsMap()).amHealthy()));
            outputArr.add(Boolean.toString(makeExpressionForAudit(PARENT_ELEMENT_CONC_CONST, parEle, squidTask.getNamedExpressionsMap()).amHealthy()));

            outputArr.add(squidTask.printTaskAudit());
            Gson packager = new Gson();
            response.getWriter().println(packager.toJson(Arrays.toString(outputArr.toArray())));
        } catch (Exception e) {
            response.getWriter().print(e);
            e.printStackTrace();
            System.out.print(e);
        }
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


