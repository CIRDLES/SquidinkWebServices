package com.cirdles;

import com.google.gson.Gson;
import org.cirdles.squid.Squid3API;
import org.cirdles.squid.Squid3Ink;
import org.cirdles.squid.projects.Squid3ProjectBasicAPI;
import org.cirdles.squid.tasks.TaskInterface;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * Servlet implementation class FileUploadServlet
 */

@WebServlet(name = "CurrentTaskStringsServlet", urlPatterns = {"/CurrentTaskStringsServlet"})
@MultipartConfig(
        fileSizeThreshold = 1024 * 1024 * 1, // MB
        maxFileSize = 1024 * 1024 * 10, // 10 MB
        maxRequestSize = 1024 * 1024 * 100 // 100 MB
)


public class CurrentTaskStringsServlet extends HttpServlet {
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
            String body = request.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
            Squid3API squid = (Squid3API) this.getServletConfig().getServletContext().getAttribute(body);
            TaskInterface squidTask = squid.getSquid3Project().getTask();
            ArrayList<String> outputArr = new ArrayList<>();
            outputArr.add(squidTask.getName());
            outputArr.add(squidTask.getDescription());
            outputArr.add(squidTask.getAuthorName());
            outputArr.add(squidTask.getLabName());
            outputArr.add(squidTask.getProvenance());
            outputArr.add(squidTask.getParentNuclide());
            outputArr.add(squidTask.isDirectAltPD() ? "direct":"indirect");
            outputArr.add(squidTask.printTaskAudit());
            Gson packager = new Gson();
            response.getWriter().println(packager.toJson(Arrays.toString(outputArr.toArray())));
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


