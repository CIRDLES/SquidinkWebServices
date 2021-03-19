package com.cirdles;

import org.zeroturnaround.zip.ZipUtil;

import java.io.*;
import java.util.stream.Collectors;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

/**
 * Servlet implementation class FileRetrieverServlet
 */
@WebServlet(name = "DownloadServlet", urlPatterns = {"/download/*"})


public class DownloadServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String contextPath = request.getPathInfo();
        //Fixes removal of _ in user_files directory relative to provided context.
        //Alternatively we can just remove the _ in the path.
        contextPath = contextPath.substring(0,15) + "_" + contextPath.substring(15);
        //Forces file download regardless of file type
        response.setContentType("application/octet-stream");
        OutputStream out = response.getOutputStream();
        FileInputStream in = new FileInputStream(new File(contextPath));
        byte[] buffer = new byte[4096];
        int length;
        while ((length = in.read(buffer)) > 0){
            out.write(buffer, 0, length);
        }
        in.close();
        out.flush();

    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

}
