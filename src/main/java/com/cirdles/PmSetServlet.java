package com.cirdles;


import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.stream.Collectors;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.cirdles.squid.Squid3API;
import org.cirdles.squid.Squid3Ink;
import org.cirdles.squid.constants.Squid3Constants;
import org.cirdles.squid.parameters.parameterModels.ParametersModel;

import javax.servlet.annotation.WebServlet;




public class PmSetServlet extends HttpServlet {
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
        String[] body = request.getReader().lines().collect(Collectors.joining(System.lineSeparator())).split(":");
        Squid3API squid = (Squid3API) this.getServletConfig().getServletContext().getAttribute(body[0]);
        try {
        switch(body[1]) {
            case "SBM":
                if(body[2].equals("true"))
                    squid.setUseSBM(true);
                else
                    squid.setUseSBM(false);
                response.getWriter().println(body[2]);
                break;
            case "LinFit":
                if(body[2].equals("true"))
                    squid.setUseLinearRegression(true);
                else
                    squid.setUseLinearRegression(false);
                break;
            case "PrefIso":
                if(body[2].equals("204Pb"))
                    squid.setPreferredIndexIsotope(Squid3Constants.IndexIsoptopesEnum.PB_204);
                else if(body[2].equals("207Pb"))
                    squid.setPreferredIndexIsotope(Squid3Constants.IndexIsoptopesEnum.PB_207);
                else
                    squid.setPreferredIndexIsotope(Squid3Constants.IndexIsoptopesEnum.PB_208);
                break;
            case "autoExclude":
                if(body[2].equals("true"))
                    squid.setAutoExcludeSpots(true);
                else
                    squid.setAutoExcludeSpots(false);
                break;
            case "minSig206":
                squid.setMinimumExternalSigma206238(Double.parseDouble(body[2]));
                break;
            case "minSig208":
                squid.setMinimumExternalSigma208232(Double.parseDouble(body[2]));
                break;
            case "commonPb":
                Iterator<ParametersModel> list = Squid3Ink.getSquidLabData().getCommonPbModels().iterator();
                boolean flag = false;
                ParametersModel model = null;
                while(list.hasNext() == true) {
                    model = list.next();
                    if(model.getModelName().equals(body[2])) {
                        squid.setDefaultCommonPbModel(model);
                        flag = true;
                        break;
                    }
                }
                if(!flag)
                    squid.setDefaultCommonPbModel(model);
                break;
            case "physConstant":
                Iterator<ParametersModel> list1 = Squid3Ink.getSquidLabData().getPhysicalConstantsModels().iterator();
                boolean flag1 = false;
                ParametersModel model1 = null;
                while(list1.hasNext() == true) {
                    model1 = list1.next();
                    if(model1.getModelName().equals(body[2])) {
                        squid.setDefaultPhysicalConstantsModel(model1);
                        flag1 = true;
                        break;
                    }
                }
                if(!flag1)
                    squid.setDefaultPhysicalConstantsModel(model1);
                break;
            case "setDefaultParam":
                squid.setDefaultParametersFromCurrentChoices();
                break;
            case "refreshModel":
                squid.refreshModelsAction();
                break;
            case "projectName":
                if(!body[2].isEmpty()) {
                    squid.getSquid3Project().setProjectName(body[2]);
                }
                break;
            case "analystName":
                if(!body[2].isEmpty()) {
                    squid.getSquid3Project().setAnalystName(body[2]);
                }
                break;
            case "notes":
                squid.getSquid3Project().setProjectNotes(body[2]);
                break;
        }
        }
        catch(Exception e) {
            response.getWriter().println(e);
        }

    }


    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "reportsServlet Servlet";
    }// </editor-fold>
}
