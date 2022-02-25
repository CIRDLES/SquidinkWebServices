package com.cirdles;

import com.google.gson.Gson;
import org.cirdles.squid.Squid3API;
import org.cirdles.squid.constants.Squid3Constants;
import org.cirdles.squid.projects.Squid3ProjectBasicAPI;
import org.cirdles.squid.tasks.Task;
import org.cirdles.squid.tasks.TaskInterface;
import org.cirdles.squid.tasks.expressions.Expression;
import org.cirdles.squid.tasks.squidTask25.TaskSquid25;
import org.cirdles.squid.utilities.IntuitiveStringComparator;
import org.cirdles.squid.utilities.fileUtilities.FileValidator;
import org.cirdles.squid.utilities.xmlSerialization.XMLSerializerInterface;
import org.xml.sax.SAXException;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.XMLConstants;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.lang.reflect.Array;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;
import com.cirdles.TaskLibraryServlet.*;

import static org.cirdles.squid.constants.Squid3Constants.SQUID_TASK_LIBRARY_FOLDER;
import static org.cirdles.squid.constants.Squid3Constants.XML_HEADER_FOR_SQUIDTASK_FILES_USING_LOCAL_SCHEMA;
import static org.cirdles.squid.tasks.expressions.builtinExpressions.BuiltInExpressionsDataDictionary.*;
import static org.cirdles.squid.tasks.expressions.builtinExpressions.BuiltInExpressionsDataDictionary.PARENT_ELEMENT_CONC_CONST_DEFAULT_EXPRESSION;
import static org.cirdles.squid.utilities.fileUtilities.FileValidator.validateXML;

/**
 * Servlet implementation class FileUploadServlet
 */

@WebServlet(name = "TaskLibraryDataServlet", urlPatterns = {"/tasklibrary"})
@MultipartConfig(
        fileSizeThreshold = 1024 * 1024 * 1, // MB
        maxFileSize = 1024 * 1024 * 10, // 10 MB
        maxRequestSize = 1024 * 1024 * 100 // 100 MB
)


public class TaskLibraryDataServlet extends HttpServlet {
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
            ArrayList<String> outputArr = new ArrayList<>();
            String body[] = request.getReader().lines().collect(Collectors.joining(System.lineSeparator())).split(":");
            Squid3API squid = (Squid3API) this.getServletConfig().getServletContext().getAttribute(body[0]);
            Squid3ProjectBasicAPI infoPull = squid.getSquid3Project();
            System.out.println(body);
            File taskFile = body[2].equals("default") ? SQUID_TASK_LIBRARY_FOLDER
                    : new File(Constants.TOMCAT_ROUTE + File.separator + "filebrowser" + File.separator + "users" + File.separator + body[0] + File.separator + body[2]);
            System.out.println(Constants.TOMCAT_ROUTE + File.separator + "filebrowser" + File.separator + "users" + File.separator + body[0] + File.separator + body[2]);
            ArrayList<TaskInterface> taskList = TaskLibraryServlet.populateListOfTasks(infoPull, taskFile);
            for(TaskInterface task : taskList) {
                if(task.getName().toLowerCase().trim().equals(body[1].toLowerCase().trim())) {
                    boolean uPicked = task.getParentNuclide().equals("238");
                    boolean directPicked = task.isDirectAltPD();;
                    boolean perm1 = uPicked && !directPicked;
                    boolean perm2 = uPicked && directPicked;
                    boolean perm3 = !uPicked && !directPicked;
                    boolean perm4 = !uPicked && directPicked;

                    outputArr.add(task.getName());
                    outputArr.add(task.getDescription());
                    outputArr.add(task.getAuthorName());
                    outputArr.add(task.getLabName());
                    outputArr.add(task.getProvenance());
                    outputArr.add(task.getParentNuclide());
                    outputArr.add(task.isDirectAltPD() ? "direct":"indirect");
                    outputArr.add(Arrays.toString(task.getNominalMasses().toArray()));
                    outputArr.add(Arrays.toString(task.getRatioNames().toArray()));

                    String UTh_U_ExpressionString = task.getSpecialSquidFourExpressionsMap().get(UNCOR206PB238U_CALIB_CONST);

                    String UTh_Th_ExpressionString = task.getSpecialSquidFourExpressionsMap().get(UNCOR208PB232TH_CALIB_CONST);

                    String thU_ExpressionString = task.getSpecialSquidFourExpressionsMap().get(TH_U_EXP_DEFAULT);

                    String parentPPM_ExpressionString = task.getSpecialSquidFourExpressionsMap().get(PARENT_ELEMENT_CONC_CONST);

                    outputArr.add((perm1 || perm2 || perm4) ? UTh_U_ExpressionString : "Not Used");
                    outputArr.add((perm2 || perm3 || perm4) ? UTh_Th_ExpressionString : "Not Used");
                    outputArr.add((perm1 || perm3) ? thU_ExpressionString : "Not Used");
                    outputArr.add(parentPPM_ExpressionString);
                }
            }
            Gson gson = new Gson();
            response.getWriter().println(gson.toJson(Arrays.toString(outputArr.toArray())));
        } catch (Exception e) {
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

