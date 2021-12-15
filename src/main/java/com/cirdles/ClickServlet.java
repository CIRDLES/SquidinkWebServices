package com.cirdles;

import org.cirdles.squid.Squid3API;
import org.cirdles.squid.Squid3Ink;
import org.cirdles.squid.exceptions.SquidException;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.stream.Collectors;

import static org.cirdles.squid.constants.Squid3Constants.DEMO_SQUID_PROJECTS_FOLDER;

/**
 * Servlet implementation class FileUploadServlet
 */

@WebServlet(name = "ClickServlet", urlPatterns = {"/ClickServlet/*"})
@MultipartConfig(
        fileSizeThreshold = 1024 * 1024 * 1, // MB
        maxFileSize = 1024 * 1024 * 10, // 10 MB
        maxRequestSize = 1024 * 1024 * 100 // 100 MB
)


public class ClickServlet extends HttpServlet {
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

            response.getWriter().println(System.getenv("CATALINA_HOME"));
            String body = request.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
            String pathToDir = System.getenv("CATALINA_HOME") + File.separator + "filebrowser" + File.separator + "users" + File.separator + body;
            this.getServletConfig().getServletContext().setAttribute(body, Squid3Ink.spillSquid3Ink(pathToDir));
            Squid3API squid = (Squid3API) this.getServletConfig().getServletContext().getAttribute(body);
            File localDemoFile = new File(DEMO_SQUID_PROJECTS_FOLDER.getAbsolutePath()
                    + File.separator + "SQUID3_demo_file.squid");
            Path basepath = localDemoFile.toPath();
            Path target = new File(
                    System.getenv("CATALINA_HOME") + File.separator + "filebrowser" + File.separator + "users"
                            + File.separator + body + File.separator + "SQUID3_demo_file.squid").toPath();
            response.getWriter().println(basepath.toString());
            response.getWriter().println(target.toString());
            Files.copy(basepath, target, StandardCopyOption.REPLACE_EXISTING);
            squid.openSquid3Project(target);
        } catch (SquidException | IOException | SecurityException e) {
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
