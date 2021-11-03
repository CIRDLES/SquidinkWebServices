package com.cirdles;

import java.io.IOException;
import java.util.stream.Collectors;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.cirdles.squid.Squid3API;
import org.cirdles.squid.Squid3Ink;
import org.cirdles.squid.exceptions.SquidException;
import org.cirdles.squid.parameters.parameterModels.ParametersModel;
import org.cirdles.squid.projects.Squid3ProjectBasicAPI;

import javax.servlet.annotation.WebServlet;

/**
 * Servlet implementation class FileUploadServlet
 */

@WebServlet(name = "ProjectManagementServlet", urlPatterns = { "/pmpull" })
@MultipartConfig(
        fileSizeThreshold = 1024 * 1024 *1, // MB
        maxFileSize = 1024 * 1024 * 10, // 10 MB
        maxRequestSize = 1024 * 1024 * 100 // 100 MB
)


public class ProjectManagementServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            String body = request.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
            Squid3API squid = (Squid3API) this.getServletConfig().getServletContext().getAttribute(body);
            Squid3ProjectBasicAPI infoPull = squid.getSquid3Project();
            String out = "";
            out += infoPull.getProjectName() + "~!@";
            out += infoPull.getAnalystName() + "~!@";
            //Missing Routes (Do we even want to include them? Maybe trim?
            out += infoPull.isUseSBM() + "~!@";
            out += infoPull.isUserLinFits() + "~!@";
            out += infoPull.getSelectedIndexIsotope() + "~!@";
            out += infoPull.isSquidAllowsAutoExclusionOfSpots() + "~!@";
            out += infoPull.getExtPErrU() + "~!@";
            out += infoPull.getExtPErrTh() + "~!@";
            out += infoPull.getCommonPbModel().getModelNameWithVersion() + "~!@";
            out += infoPull.getPhysicalConstantsModel().getModelNameWithVersion() + "~!@";
            out += infoPull.getSessionDurationHours() + "~!@";
            out += infoPull.getProjectNotes() + "~!@";
            out += infoPull.getPrawnFileShrimpSoftwareVersionName() + "~!@";
            out += infoPull.getPrawnSourceFilePath() + "~!@";
            out += infoPull.generatePrefixTreeFromSpotNames().buildSummaryDataString() + "~!@";
            for (ParametersModel model : Squid3Ink.getSquidLabData().getCommonPbModels()) {
                out += model.getModelNameWithVersion() + "*&^";
            }
            out += "~!@";
            for (ParametersModel model : Squid3Ink.getSquidLabData().getPhysicalConstantsModels()) {
                out += model.getModelNameWithVersion() + "*&^";
            }
            response.getWriter().println(out);
        }
        catch(SquidException e) {
            e.printStackTrace();
            response.getWriter().print(e);
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
