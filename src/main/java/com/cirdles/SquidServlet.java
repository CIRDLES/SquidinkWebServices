package com.cirdles;

import java.io.*;
import java.io.File;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.JAXBException;
import org.apache.commons.io.IOUtils;
import org.cirdles.squid.web.SquidReportingService;
import org.springframework.web.bind.ServletRequestUtils;
import org.xml.sax.SAXException;
import javax.servlet.annotation.WebServlet;

/**
 * Servlet implementation class FileUploadServlet
 */

@WebServlet(name = "SquidServlet", urlPatterns = { "/squidServlet/*" })
@MultipartConfig(
        fileSizeThreshold = 1024 * 1024 *1, // MB
        maxFileSize = 1024 * 1024 * 10, // 10 MB
        maxRequestSize = 1024 * 1024 * 100 // 100 MB
)


public class SquidServlet extends HttpServlet {
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

        // this seems to hang needs work HttpSession session = request.getSession();
        response.setContentType("application/zip");
        response.setHeader("Content-Disposition", "attachment; filename=squid-reports.zip");

        try {
            boolean useSBM = ServletRequestUtils.getBooleanParameter(request, "NormIonCounts", true);
            boolean useLinFits = ServletRequestUtils.getBooleanParameter(request, "UseLinFits", false);
            String refMatFilter = ServletRequestUtils.getStringParameter(request, "RefMatSamp", "");
            String concRefMatFilter = ServletRequestUtils.getStringParameter(request, "ConcRefMatSamp", "");
            String preferredIndexIsotopeName = ServletRequestUtils.getStringParameter(request, "prefIndexIso", "PB_204");
            String filePart = request.getParameter("prawnFile");
            String filePart2 = request.getParameter("taskFile");

            String fileName = filePart;
            InputStream fileStream = new FileInputStream(new File("/usr/local/user_files/" + filePart));
            InputStream fileStream2 = new FileInputStream(new File("/usr/local/user_files/" + filePart2));

            SquidReportingService handler = new SquidReportingService();

            File report = null;
            report = handler.generateReports(
                    "WebProject", fileName, fileStream, fileStream2, useSBM, useLinFits, refMatFilter, concRefMatFilter,
                    preferredIndexIsotopeName).toFile();

//            // now if Linux. we are going to assume cirdles.cs.cofc.edu and write to Google Drive for now
//            // note: gdrive runs as root: sudo chmod +s gdrive
//            Thread thread = new Thread() {
//                public void run() {
//                    System.out.println("Thread Running");
//                    try {
//                        String[] arguments = new String[]{
//                            System.getenv("CATALINA_HOME") + "/gdrive", "mkdir", "JIMMY"};
//                        //"/home/gdrive", "upload", "--parent", "19RHlWggIw5fqWQUO1xs3M2iWjD82Ph3m", "/opt/tomcat9/temp/reports.zip"};
//                        List<String> argList = Arrays.asList(arguments);
//
//                        ProcessBuilder processBuilder = new ProcessBuilder(argList);
//                        Process process = processBuilder.start();
//
//                        int exitCode = process.waitFor();
//                        assert exitCode == 0;
//
//                    } catch (IOException | InterruptedException iOException) {
//                    }
//                }
//            };
//
//            thread.start();
            File target = new File("/usr/local/user_files/" + report.getName());
            OutputStream out = new FileOutputStream(target);
            IOUtils.copy(new FileInputStream(report), out);
            out.close();
            response.getWriter().println("We are done here.");

        } catch (IOException | JAXBException | SAXException e) {
            System.err.println(e);
        }
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Squid Reporting Servlet";
    }// </editor-fold>
}
