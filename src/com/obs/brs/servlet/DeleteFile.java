package com.obs.brs.servlet;

import java.io.File;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.obs.brs.utils.CommonProperties;

/**
 * Servlet implementation class DeleteFile
 */
@WebServlet("/DeleteFile")
public class DeleteFile extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public DeleteFile() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String filePath = CommonProperties.getBasePath()+CommonProperties.getImageContextPath()+CommonProperties.getParentImageTempPath();
		String name=request.getParameter("id");
		String userId=request.getParameter("userId");
		String imagePath=filePath+"/"+userId+"/"+name;
		File deleteFile = new File(imagePath);
		// check if the file is present or not
		if(deleteFile.exists())
		deleteFile.delete() ;
		System.out.println("Delete file"+name+"userId==>"+userId);
		
		
	}

}
