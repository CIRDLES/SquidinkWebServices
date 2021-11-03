package com.cirdles;

import org.cirdles.squid.Squid3API;
import org.cirdles.squid.Squid3Ink;
import org.cirdles.squid.exceptions.SquidException;
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

@WebServlet(name = "SpotsRmTablesServlet", urlPatterns = {"/spotstables"})
@MultipartConfig(
        fileSizeThreshold = 1024 * 1024 * 1, // MB
        maxFileSize = 1024 * 1024 * 10, // 10 MB
        maxRequestSize = 1024 * 1024 * 100 // 100 MB
)


public class SpotsRmTablesServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request  servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException      if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">

    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request  servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException      if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

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
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            String body[] = request.getReader().lines().collect(Collectors.joining(System.lineSeparator())).split("!@#");
            Squid3API squid = (Squid3API) this.getServletConfig().getServletContext().getAttribute(body[0]);
            String clear = "";
            if (body[1].equals("RM")) {
                if (body[2].equals("clear")) {
                    squid.setReferenceMaterialSampleName(clear);
                    for (ParametersModel model : Squid3Ink.getSquidLabData().getReferenceMaterials()) {
                        if (model.getModelName() == "NONE") {
                            squid.updateRefMatModelChoice(model);
                        }
                    }

                } else {
                    squid.setReferenceMaterialSampleName(body[2]);
                }

            } else {
                if (body[2].equals("clear")) {
                    squid.setConcReferenceMaterialSampleName(clear);
                    for (ParametersModel model : Squid3Ink.getSquidLabData().getReferenceMaterials()) {
                        if (model.getModelName() == "NONE") {
                            squid.updateConcRefMatModelChoice(model);
                        }
                    }
                } else {
                    squid.setConcReferenceMaterialSampleName(body[2]);
                }
            }
            response.getWriter().println("Replaced " + body[1] + " Sample Name with " + body[2]);
        } catch (SquidException e) {
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
