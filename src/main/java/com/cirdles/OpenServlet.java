package com.cirdles;

import java.io.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.stream.Collectors;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.JAXBException;
import org.apache.commons.io.IOUtils;
import org.cirdles.squid.Squid;
import org.cirdles.squid.Squid3API;
import org.cirdles.squid.Squid3Ink;
import org.cirdles.squid.exceptions.SquidException;
import org.cirdles.squid.web.SquidReportingService;
import org.eclipse.jetty.server.Response;
import org.springframework.web.bind.ServletRequestUtils;
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
        generateSquid3API();
        System.out.println(System.getProperty("user.dir"));
        System.setProperty("user.home", "/usr/local/user_files");
        generateSquid3API();
        String requestURI[] = request.getRequestURI().split("/");
        String context = requestURI[requestURI.length - 1];
        String curPath = (String) this.getServletConfig().getServletContext().getAttribute("curPath");
        String body = request.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
        Squid3API squid = (Squid3API) this.getServletConfig().getServletContext().getAttribute("squid3API");
        System.out.println(Squid.VERSION);
        File path = new File(curPath + File.separator + body);
        String[] isZip = path.toString().split("\\.");
        try {
        if (isZip[isZip.length - 1].equals("zip")) {
            System.out.println("This Is Zipped");
            response.getWriter().println("zipped");
                squid.newSquid3GeochronProjectFromZippedPrawnXML(path.toPath());
        }
        else if(isZip[isZip.length - 1].equals("squid")) {
            squid.openSquid3Project(path.toPath());
        }
        else {
            squid.newSquid3GeochronProjectFromPrawnXML(path.toPath());
        }
        }
        catch(JAXBException | SAXException | SquidException e) {
            response.getWriter().println(e);
            e.printStackTrace();
        }

    }

    private void generateSquid3API() {
        if(this.getServletConfig().getServletContext().getAttribute("squid3API") == null) {
            this.getServletConfig().getServletContext().setAttribute("squid3API", Squid3Ink.spillSquid3Ink());
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


