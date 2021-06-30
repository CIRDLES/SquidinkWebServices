package com.cirdles;

import java.io.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.stream.Collectors;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.JAXBException;
import org.cirdles.squid.Squid;
import org.cirdles.squid.Squid3API;
import org.cirdles.squid.Squid3Ink;
import org.cirdles.squid.exceptions.SquidException;
import org.xml.sax.SAXException;
import javax.servlet.annotation.WebServlet;

/**
 * Servlet implementation class FileUploadServlet
 */

@WebServlet(name = "ClickServlet", urlPatterns = { "/OpenServlet/*" })
@MultipartConfig(
        fileSizeThreshold = 1024 * 1024 *1, // MB
        maxFileSize = 1024 * 1024 * 10, // 10 MB
        maxRequestSize = 1024 * 1024 * 100 // 100 MB
)


public class OpenServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

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
        String curPath =
                System.getenv("CATALINA_HOME") + File.separator + "filebrowser" + File.separator + "users" + File.separator + body[0];
        Squid3API squid = (Squid3API) this.getServletConfig().getServletContext().getAttribute(body[0]);
        //tomcat/filebrowser/users/userfolder/selectedfile
        try {
            File path = new File(curPath + File.separator + body[1] + File.separator);
            String[] isZip = path.toString().split("\\.");
            response.getWriter().println(path.toString());
        if (isZip[isZip.length - 1].equals("zip")) {
            response.getWriter().println("zipped");
            squid.newSquid3GeochronProjectFromZippedPrawnXML(path.toPath());
        }
        else if(isZip[isZip.length - 1].equals("squid")) {
            response.getWriter().println(path.toString());
            squid.openSquid3Project(path.toPath());
        }
        else {
            response.getWriter().println(path.toString());
            squid.newSquid3GeochronProjectFromPrawnXML(path.toPath());
        }
        }
        catch(Exception e) {
            response.getWriter().println(e);
            e.printStackTrace();
        }
        this.getServletConfig().getServletContext().setAttribute("squid3API", squid);
        response.getWriter().println("End");
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


