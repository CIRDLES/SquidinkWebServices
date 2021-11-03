package com.cirdles;


import java.io.IOException;
import java.util.stream.Collectors;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.cirdles.squid.Squid3API;
import org.cirdles.squid.Squid3Ink;
import javax.servlet.annotation.WebServlet;

/**
 * Servlet implementation class FileUploadServlet
 */

@WebServlet(name = "IndividReportServlet", urlPatterns = { "/IndividReportServlet" })
@MultipartConfig(
        fileSizeThreshold = 1024 * 1024 *1, // MB
        maxFileSize = 1024 * 1024 * 10, // 10 MB
        maxRequestSize = 1024 * 1024 * 100 // 100 MB
)


public class IndividReportServlet extends HttpServlet {
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
            if (body[1].equals("RefMat")) {
                response.getWriter().println(squid.generateReferenceMaterialSummaryExpressionsReport().toString());
            } else if (body[1].equals("Unknown")) {
                response.getWriter().println(squid.generateUnknownsSummaryExpressionsReport().toString());
            } else if (body[1].equals("ProjectAudit")) {
                response.getWriter().println(squid.generateProjectAuditReport().toString());
            } else if (body[1].equals("TaskAudit")) {
                response.getWriter().println(squid.generateTaskSummaryReport().toString());
            } else if (body[1].equals("PerScan")) {
                response.getWriter().println(squid.generatePerScanReports().toString());
            }
        }
        catch(Exception e) {
            e.printStackTrace();
            response.getWriter().println(e);
        }
        response.getWriter().println("done");
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
