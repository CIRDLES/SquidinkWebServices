package com.cirdles;

import com.google.gson.Gson;
import org.cirdles.squid.Squid3API;
import org.cirdles.squid.constants.Squid3Constants;
import org.cirdles.squid.projects.Squid3ProjectBasicAPI;
import org.cirdles.squid.tasks.Task;
import org.cirdles.squid.tasks.TaskInterface;
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

import static org.cirdles.squid.constants.Squid3Constants.SQUID_TASK_LIBRARY_FOLDER;
import static org.cirdles.squid.constants.Squid3Constants.XML_HEADER_FOR_SQUIDTASK_FILES_USING_LOCAL_SCHEMA;
import static org.cirdles.squid.utilities.fileUtilities.FileValidator.validateXML;

/**
 * Servlet implementation class FileUploadServlet
 */

@WebServlet(name = "TaskLibraryServlet", urlPatterns = {"/tasklibrary"})
@MultipartConfig(
        fileSizeThreshold = 1024 * 1024 * 1, // MB
        maxFileSize = 1024 * 1024 * 10, // 10 MB
        maxRequestSize = 1024 * 1024 * 100 // 100 MB
)


public class TaskLibraryServlet extends HttpServlet {
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
            ArrayList<String> outputList = new ArrayList<>();
            String[] body = request.getReader().lines().collect(Collectors.joining(System.lineSeparator())).split(":");
            Squid3API squid = (Squid3API) this.getServletConfig().getServletContext().getAttribute(body[0]);

            Squid3ProjectBasicAPI infoPull = squid.getSquid3Project();
            File taskFile = body[1].equals("default") ? SQUID_TASK_LIBRARY_FOLDER
                    : new File(Constants.TOMCAT_ROUTE + File.separator + "filebrowser" + File.separator + "users" + File.separator + body[0] + File.separator + body[1]);
            ArrayList<TaskInterface> taskList = populateListOfTasks(infoPull, taskFile);
            for(TaskInterface listItem : taskList) {
                outputList.add(listItem.getName());
            }
            String[] newOutput = outputList.toArray(new String[0]);
            Arrays.sort(newOutput, new IntuitiveStringComparator<String>());
            Gson gson = new Gson();
            response.getWriter().println(gson.toJson(Arrays.toString(newOutput)));
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

    public static ArrayList<TaskInterface> populateListOfTasks(Squid3ProjectBasicAPI squidProject, File route) {
        SchemaFactory sf = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        Schema taskXMLSchema = null;
        try {
            taskXMLSchema = sf.newSchema(new File(Squid3Constants.URL_STRING_FOR_SQUIDTASK_XML_SCHEMA_LOCAL));
        } catch (SAXException e) {
            e.printStackTrace();
        }

        ArrayList<TaskInterface> taskFilesInFolder = new ArrayList<>();
        File tasksBrowserTarget = route;
        String tasksBrowserType = ".xml";
        if (tasksBrowserTarget != null) {
            if (tasksBrowserType.compareToIgnoreCase(".xml") == 0) {
                if (tasksBrowserTarget.isDirectory()) {
                    // collect Tasks if any
                    for (File file : tasksBrowserTarget.listFiles(new FilenameFilter() {
                                                                      @Override
                                                                      public boolean accept(File file, String name) {
                                                                          return name.toLowerCase().endsWith(".xml");
                                                                      }
                                                                  }
                    )) {
                        // check if task
                        try {
                            TaskInterface task = (Task) ((XMLSerializerInterface)  // Filtering out non-Task XML files
                                    squidProject.getTask()).readXMLObject(file.getAbsolutePath(), false);
                            if (task != null) {
                                FileValidator.validateXML(file, taskXMLSchema, XML_HEADER_FOR_SQUIDTASK_FILES_USING_LOCAL_SCHEMA);
                                taskFilesInFolder.add(task); // Not added if exception thrown from validateXML
                            }
                        } catch (IOException | ArrayIndexOutOfBoundsException | SAXException e) {
                        }
                    } // End for loop for files
                } else {
                    // check if task
                    try {
                        TaskInterface task = (Task) ((XMLSerializerInterface)  // Filtering out non-Task XML files
                                squidProject.getTask()).readXMLObject(tasksBrowserTarget.getAbsolutePath(), false);
                        if (task != null) {
                            validateXML(tasksBrowserTarget, taskXMLSchema, XML_HEADER_FOR_SQUIDTASK_FILES_USING_LOCAL_SCHEMA);
                            taskFilesInFolder.add(task); // Not added if exception thrown from validateXML
                        }
                    } catch (IOException | ArrayIndexOutOfBoundsException | SAXException e) {
                    }
                }
            }
        }
        return taskFilesInFolder;

        }
    }

