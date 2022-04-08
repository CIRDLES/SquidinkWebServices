package com.cirdles;


import org.cirdles.squid.Squid3API;
import org.cirdles.squid.exceptions.SquidException;
import org.cirdles.squid.tasks.Task;
import org.cirdles.squid.tasks.TaskInterface;
import org.cirdles.squid.utilities.stateUtilities.SquidPersistentState;
import org.cirdles.squid.utilities.xmlSerialization.XMLSerializerInterface;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.stream.Collectors;


public class SaveXMLServlet extends HttpServlet {
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
                Constants.TOMCAT_ROUTE + File.separator + "filebrowser" + File.separator + "users" + File.separator + body[0] + File.separator + body[1];
        Squid3API squid = (Squid3API) this.getServletConfig().getServletContext().getAttribute(body[0]);
        try {
            File taskFileXML = new File(curPath);
            taskFileXML.createNewFile();
            TaskInterface interim;
            if(body.length > 2 && body[2].equals("current")) {
                interim = null;
            } else {
                interim = (TaskInterface) this.getServletConfig().getServletContext().getAttribute(body[0] + "task");
            }
            TaskInterface task = interim == null? squid.getSquid3Project().getTask(): interim;
            if (taskFileXML != null) {
                ((XMLSerializerInterface) task)
                        .serializeXMLObject(taskFileXML.getAbsolutePath());
            }
        }
        catch(Exception e) {
            System.out.println(e);
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
