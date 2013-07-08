package com.lrntothink;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ShowFileContent extends HttpServlet{
	private String filePath;
	private String allowFileType = " txt log c cfg sdf sql h sql ini css js inf ";
	private String binFileType = " htm html jpg jpeg gif ico png ";
    public void doPost(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {
    	doGet(request,response);
    }
    public void doGet(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {
    	this.filePath = request.getParameter("filepath");
//        System.out.println(filePath);
        
        File file = new File(filePath);
        
        if(file.isFile()){
        	String fileName = file.getName();
        	if(fileName.indexOf(".")!=-1){
        		String fileType = fileName.substring(fileName.lastIndexOf(".")+1).toLowerCase();
        		if(binFileType.contains(" "+fileType+" ")){
        			response.setCharacterEncoding("gbk");
        			OutputStream outStream = response.getOutputStream();
        			try{
        				FileInputStream fip = new FileInputStream(filePath);
        				byte[] buffer = new byte[1024]; // 建立缓冲区
        				int len;
        				while ((len = fip.read(buffer)) != -1){
        					outStream.write(buffer, 0, len);
        				}
        				fip.close();
        				outStream.close();
        				// 关闭输入流,释放系统资源
        			}catch(Exception e) {
        				System.out.println(e.getStackTrace());
        			} 
        		}else if(allowFileType.contains(" "+fileType+" ")){
        			response.setHeader("content-type", "text/html;charset=UTF-8");
        			response.setContentType( "text/html;charset=UTF-8");
        			PrintWriter out = response.getWriter();
        			BufferedReader reader= new BufferedReader(new InputStreamReader( new FileInputStream(filePath),"gbk"));
        			String line= null;
                    while ((line= reader.readLine()) != null) {
                        out.println("\t"+line+"<br>");
                   }
                   reader.close();
                   out.close();
        		}else{
        			response.setHeader("content-type", "text/plain;charset=UTF-8");
        			response.setContentType( "text/plain;charset=UTF-8");
        			PrintWriter out = response.getWriter();
        			out.println("你选择了："+fileName);
        			out.println("文件类型为:"+fileType+" ，不能打开。");
        			out.println("支持的文件类型有："+allowFileType+binFileType);
        			out.close();
        		}
        	}
        }else{
        	response.setHeader("content-type", "text/plain;charset=UTF-8");
			response.setContentType("text/plain;charset=UTF-8");
			PrintWriter out = response.getWriter();
			File[] f = file.listFiles();
			out.println(file.getName()+"是文件夹，包含以下文件或文件夹：");
			for(File f1:f){
				out.println(f1.getName());
			}
			out.close();
		}
		
    }
}
