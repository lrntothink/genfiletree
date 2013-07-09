package com.lrntothink;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class DownloadFile extends HttpServlet{
	
    public void doPost(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {
    	doGet(request,response);
    }
    public void doGet(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {
    	String filename = request.getParameter("filename");
//        System.out.println(filename);
        
    	try {
            response.reset();
            //add 增加报表下载中文名称
            response.setHeader("Content-Disposition", "attachment; filename=" + new String(filename.getBytes("GBK"),"ISO8859_1"));
            //end
            ServletOutputStream os = response.getOutputStream();
            FileInputStream in = new FileInputStream(filename);
            byte[] data = new byte[1024];
            int temp = -1;
            while((temp = in.read(data))!= -1){
                  os.write(data, 0, temp);
                  os.flush();
            }
            in.close();
            os.close();             
      } catch(Exception ex) {
            ex.printStackTrace();
      }

    }
	
}
