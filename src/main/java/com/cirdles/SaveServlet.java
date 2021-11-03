package com.cirdles;


import org.cirdles.squid.Squid3API;
import org.cirdles.squid.exceptions.SquidException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Collectors;


public class SaveServlet extends HttpServlet {
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
        String[] body = request.getReader().lines().collect(Collectors.joining(System.lineSeparator())).split(":");
        String curPath =
                System.getenv("CATALINA_HOME") + File.separator + "filebrowser" + File.separator + "users" + File.separator + body[0];
        Squid3API squid = (Squid3API) this.getServletConfig().getServletContext().getAttribute(body[0]);
        try {
            squid.saveCurrentSquid3Project();
            Path source = Paths.get(curPath + File.separator + body[1] + File.separator);
            String siblingPath = (curPath + File.separator + body[1]);
            int index = siblingPath.lastIndexOf("/");
            if (index >= 0)
                siblingPath = siblingPath.substring(0, index);
            Files.move(source, source.resolveSibling(siblingPath + File.separator + squid.getSquid3Project().getProjectName() + ".squid"));
        } catch (Exception e) {
            //Fall-back case for .xml/zip files that do not already have a present .squid file to reference
            try {

                File newSquidFile = new File(curPath + File.separator + body[1]);
                squid.saveAsSquid3Project(newSquidFile);
            } catch (SquidException | IOException | SecurityException ex) {
                response.getWriter().println(ex);
                ex.printStackTrace();
            }
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
