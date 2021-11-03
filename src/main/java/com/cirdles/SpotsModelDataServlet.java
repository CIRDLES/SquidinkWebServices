package com.cirdles;

import java.io.IOException;
import java.sql.Ref;
import java.util.stream.Collectors;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.cirdles.squid.Squid3API;
import org.cirdles.squid.Squid3Ink;
import org.cirdles.squid.parameters.parameterModels.ParametersModel;
import org.cirdles.squid.parameters.parameterModels.referenceMaterialModels.ReferenceMaterialModel;
import org.cirdles.squid.projects.Squid3ProjectBasicAPI;

import javax.servlet.annotation.WebServlet;

/**
 * Servlet implementation class FileUploadServlet
 */

@WebServlet(name = "SpotsModelDataServlet", urlPatterns = { "/spotsdata" })
@MultipartConfig(
        fileSizeThreshold = 1024 * 1024 *1, // MB
        maxFileSize = 1024 * 1024 * 10, // 10 MB
        maxRequestSize = 1024 * 1024 * 100 // 100 MB
)


public class SpotsModelDataServlet extends HttpServlet {
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
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            String body[] = request.getReader().lines().collect(Collectors.joining(System.lineSeparator())).split("!@#");
            Squid3API squid = (Squid3API) this.getServletConfig().getServletContext().getAttribute(body[0]);
            Gson gson = new Gson();
            //Find corresponding RM ParametersModel Object
            for (ParametersModel model : Squid3Ink.getSquidLabData().getReferenceMaterials()) {
                if (model.getModelNameWithVersion().equals(body[1])) {
                    ReferenceMaterialModel curModel = (ReferenceMaterialModel) model;
                    response.getWriter().println(gson.toJson(squid.produceAuditOfRefMatModel(curModel)));
                    response.getWriter().println(gson.toJson(squid.get206_238DateMa(curModel)));
                    response.getWriter().println(gson.toJson(squid.get207_206DateMa(curModel)));
                    response.getWriter().println(gson.toJson(squid.get208_232DateMa(curModel)));
                    response.getWriter().println(gson.toJson(squid.get238_235Abundance(curModel)));
                }
            }
            for (ParametersModel model : Squid3Ink.getSquidLabData().getReferenceMaterialsWithNonZeroConcentrations()) {
                if (model.getModelNameWithVersion().equals(body[2])) {
                    ReferenceMaterialModel curModel = (ReferenceMaterialModel) model;
                    response.getWriter().println(gson.toJson(squid.getU_ppm(curModel)));
                    response.getWriter().println(gson.toJson(squid.getTh_ppm(curModel)));
                }
            }
            response.getWriter().println(gson.toJson(squid.getSquid3Project().getReferenceMaterialModel().getModelNameWithVersion()));
            response.getWriter().println(gson.toJson(squid.getSquid3Project().getConcentrationReferenceMaterialModel().getModelNameWithVersion()));
        }
        catch(Exception e) {
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
