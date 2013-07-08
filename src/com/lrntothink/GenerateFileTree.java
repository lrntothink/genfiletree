package com.lrntothink;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class GenerateFileTree extends HttpServlet{
	private String filePath;
	private long loopNums = 0; 
	private ArrayList<ArrayList<String>> al = null;
	
    public void doPost(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {
    	doGet(request,response);
    }
    public void doGet(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {
    	this.filePath = request.getParameter("filepath");
        System.out.println(filePath);
        
        al = new ArrayList<ArrayList<String>>();
		File file = new File(filePath);
		genFileTree(file,"root","0");
		
        request.setAttribute("resultSet", al);
        request.setAttribute("filepath", filePath);
        RequestDispatcher setuppage = request.getRequestDispatcher("genfiletree.jsp" );
        setuppage.forward(request, response);
    }
	
	private void genFileTree(File filePath ,String parentNode,String node){
		ArrayList<String> list = new ArrayList<String>();
		if(filePath.isDirectory()){
			list.add(parentNode);
			list.add(node);
			list.add(filePath.getName());
			list.add("folder");
			list.add(filePath.getAbsolutePath());
			al.add(list);
			loopNums++;
			File[] file = filePath.listFiles();
			for(int j=0;j<file.length;j++){
				genFileTree(file[j],node,"d"+node+"w"+j);
			}
			loopNums--;
		}else if(filePath.isFile()){
			list.add(parentNode);
			list.add(node);
			list.add(filePath.getName());
			list.add("file");
			list.add(filePath.getAbsolutePath());
			al.add(list);
		}
	}
}
