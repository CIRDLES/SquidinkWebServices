package com.cirdles;

import org.cirdles.squid.Squid3API;
import org.cirdles.squid.Squid3Ink;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * Servlet implementation class FileUploadServlet
 */

@WebServlet(name = "ClickServlet", urlPatterns = {"/OpenServlet/*"})
@MultipartConfig(
        fileSizeThreshold = 1024 * 1024 * 1, // MB
        maxFileSize = 1024 * 1024 * 10, // 10 MB
        maxRequestSize = 1024 * 1024 * 100 // 100 MB
)


public class OpenServlet extends HttpServlet {
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
            response.getWriter().println(System.getenv("CATALINA_HOME"));
            String[] body = request.getReader().lines().collect(Collectors.joining(System.lineSeparator())).split(":");
            String pathToDir = Constants.TOMCAT_ROUTE + File.separator + "filebrowser" + File.separator + "users" + File.separator + body[0];
            response.getWriter().println(pathToDir);

            this.getServletConfig().getServletContext().setAttribute(body[0], Squid3Ink.spillSquid3Ink(pathToDir));
            Squid3API squid = (Squid3API) this.getServletConfig().getServletContext().getAttribute(body[0]);

            //tomcat/filebrowser/users/userfolder/selectedfile
            File path = new File(pathToDir + File.separator + body[1] + File.separator);
            String[] isZip = path.toString().split("\\.");
            System.out.println(path.toString());
            if (isZip[isZip.length - 1].equals("zip")) {
                squid.newSquid3GeochronProjectFromZippedPrawnXML(path.toPath());
            } else if (isZip[isZip.length - 1].equals("squid")) {
                squid.openSquid3Project(path.toPath());
            } else if (isZip[isZip.length - 1].equals("xml")) {
                squid.newSquid3GeochronProjectFromPrawnXML(path.toPath());
            }
              else {
                squid.newSquid3GeochronProjectFromDataFileOP(path.toPath());
            }
            this.getServletConfig().getServletContext().setAttribute("squid3API", squid);
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


