package com.cirdles;

import com.google.gson.Gson;
import org.cirdles.squid.Squid3API;
import org.cirdles.squid.Squid3Ink;
import org.cirdles.squid.parameters.parameterModels.ParametersModel;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.stream.Collectors;

/**
 * Servlet implementation class FileUploadServlet
 */

@WebServlet(name = "SpotsPullServlet", urlPatterns = { "/spotspull" })
@MultipartConfig(
        fileSizeThreshold = 1024 * 1024 *1, // MB
        maxFileSize = 1024 * 1024 * 10, // 10 MB
        maxRequestSize = 1024 * 1024 * 100 // 100 MB
)


public class SpotsPullServlet extends HttpServlet {
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
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setCharacterEncoding("UTF-8");
        String body = request.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
        Squid3API squid = (Squid3API) this.getServletConfig().getServletContext().getAttribute(body);
        Gson gson = new Gson();
        response.getWriter().println(gson.toJson(squid.getArrayOfSampleNames()));
        response.getWriter().println(gson.toJson(squid.getArrayOfSpotSummariesFromSample("ALL SAMPLES")));
        response.getWriter().println(gson.toJson(squid.getArrayOfSpotSummariesFromSample(squid.getReferenceMaterialSampleName())));
        response.getWriter().println(gson.toJson(squid.getArrayOfSpotSummariesFromSample(squid.getConcReferenceMaterialSampleName())));
        response.getWriter().println(gson.toJson(squid.getReferenceMaterialSampleName()));
        response.getWriter().println(gson.toJson(squid.getConcReferenceMaterialSampleName()));
        String out = "";
        for( ParametersModel model: Squid3Ink.getSquidLabData().getReferenceMaterials()) {
            out+= model.getModelNameWithVersion() + "!@#";
        }
        response.getWriter().println(out.substring(0,out.length() - 3));
        out = "";
        for( ParametersModel model: Squid3Ink.getSquidLabData().getReferenceMaterialsWithNonZeroConcentrations()) {
            out+= model.getModelNameWithVersion() + "!@#";
        }
        response.getWriter().println(out.substring(0,out.length() - 3));
        response.getWriter().println(gson.toJson(squid.getSquid3Project().getReferenceMaterialModel().getModelNameWithVersion()));
        response.getWriter().println(gson.toJson(squid.getSquid3Project().getConcentrationReferenceMaterialModel().getModelNameWithVersion()));
        response.getWriter().println(gson.toJson(squid.getRemovedSpotsByName().isEmpty()));
        }
        catch(Exception e) {
            response.getWriter().println(e);
            e.printStackTrace();
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
