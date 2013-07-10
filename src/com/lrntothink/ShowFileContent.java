package com.lrntothink;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.HWPFDocumentCore;
import org.apache.poi.hwpf.converter.PicturesManager;
import org.apache.poi.hwpf.converter.WordToHtmlConverter;
import org.apache.poi.hwpf.converter.WordToHtmlUtils;
import org.apache.poi.hwpf.usermodel.Picture;
import org.apache.poi.hwpf.usermodel.PictureType;
import org.w3c.dom.Document;

public class ShowFileContent extends HttpServlet{
	private String filePath;
	private String allowFileType = " txt log c cfg sdf sql h sql ini css js inf java  jsp xml inc ";
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
        		
        		if(fileType.equals("html")||fileType.equals("htm")){
        			BufferedReader reader= new BufferedReader(new InputStreamReader( new FileInputStream(file),"utf-8"));
        			String line= null;
        			StringBuilder sb = new StringBuilder();
                    while ((line= reader.readLine()) != null) {
                    	
                    	Pattern p = Pattern.compile("<img\\s+[^<>]*src[\\s\\t]*=[\\s\\t]*[\"']?([^<>\"']*)[\"']?[^<>]*>",Pattern.CASE_INSENSITIVE);
                		Matcher m = p.matcher(line);
                		if(m.find()){
                			String imgfile = m.group(1);
                			String srcFile = file.getParent()+"/"+imgfile;
                			String projectPath = request.getSession().getServletContext().getRealPath("/");
//                			System.out.println(projectPath);
                			File destFile = new File(projectPath+"/imagesTmp/"+imgfile);
                			if(!destFile.getParentFile().exists()){
                				destFile.getParentFile().mkdirs();
                			}
                			InputStream fis = new FileInputStream(new File(srcFile));
                		      FileOutputStream fos = new FileOutputStream(destFile, true);
                		       byte[] buf = new byte[8192];
                		       int size = fis.read(buf);
                		       while (size != -1) {
                		            fos.write(buf, 0, size);
                		            size = fis.read(buf);
                		      }
                		      fis.close();
                		      fos.close();
                			
                			line = line.replaceAll(imgfile, "imagesTmp/"+imgfile);
                		}
                		sb.append(line);
                   }
                    
                    request.setAttribute("filecontent", sb);
                    RequestDispatcher setuppage = request.getRequestDispatcher("showfilecontent.jsp" );
                    setuppage.forward(request, response); 
                    
                    reader.close();
        		}else if(fileType.equals("doc")){
        			
        			String projectPath = request.getSession().getServletContext().getRealPath("/");
        			try{
        			InputStream input = new FileInputStream(file);
        			HWPFDocument wordDocument = new HWPFDocument(input);
        			WordToHtmlConverter wordToHtmlConverter = new WordToHtmlConverter(DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument());
        			wordToHtmlConverter.setPicturesManager(new PicturesManager() {
        				public String savePicture(byte[] content, PictureType pictureType, String suggestedName, float widthInches, float heightInches) {
        					return suggestedName;
        				}
        			});
        			wordToHtmlConverter.processDocument(wordDocument);
        			List pics = wordDocument.getPicturesTable().getAllPictures();
        			if (pics != null) {
        				for (int i = 0; i < pics.size(); i++) {
        					Picture pic = (Picture) pics.get(i);
        					try {
        						pic.writeImageContent(new FileOutputStream(projectPath + pic.suggestFullFileName()));
        					} catch (FileNotFoundException e) {
        						e.printStackTrace();
        					}
        				}
        			}
        			Document htmlDocument = wordToHtmlConverter.getDocument();
        			ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        			DOMSource domSource = new DOMSource(htmlDocument);
        			StreamResult streamResult = new StreamResult(outStream);

        			TransformerFactory tf = TransformerFactory.newInstance();
        			Transformer serializer = tf.newTransformer();
        			serializer.setOutputProperty(OutputKeys.ENCODING, "utf-8");
        			serializer.setOutputProperty(OutputKeys.INDENT, "yes");
        			serializer.setOutputProperty(OutputKeys.METHOD, "html");
        			serializer.transform(domSource, streamResult);
        			outStream.flush();
        			outStream.close();
        			
        			response.setHeader("content-type", "text/html;charset=UTF-8");
        			response.setContentType( "text/html;charset=UTF-8");
        			PrintWriter out = response.getWriter();
        			out.println(new String(outStream.toByteArray()));
        			
        			}catch(Exception e){
        				e.printStackTrace();
        			}
        		}else if(binFileType.contains(" "+fileType+" ")){
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
//                    	if(line.trim().equals("")){
//                    		continue;
//                    	}
                    	line = line.replaceAll("<", "&lt;");
                    	line = line.replaceAll(">", "&gt;");
                        out.println(line+"<br>");
                   }
                   reader.close();
                   out.close();
        		}else{
        			response.setHeader("content-type", "text/plain;charset=UTF-8");
        			response.setContentType( "text/plain;charset=UTF-8");
        			PrintWriter out = response.getWriter();
        			out.println("你选择了："+fileName+"  类型为:"+fileType+" ，不能打开。");
        			out.println("路径："+file.getAbsolutePath());
        			out.println("支持的文件类型有："+allowFileType+binFileType);
        			out.close();
        		}
        	}
        }else{
        	response.setHeader("content-type", "text/plain;charset=UTF-8");
			response.setContentType("text/plain;charset=UTF-8");
			PrintWriter out = response.getWriter();
			File[] f = file.listFiles();
			out.println("你选择了文件夹："+file.getName()+"  路径："+file.getAbsolutePath());
			out.println("此文件夹包含以下文件或文件夹：");
			for(File f1:f){
				out.println(f1.getName());
			}
			out.close();
		}
		
    }
}
