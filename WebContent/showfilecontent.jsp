
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
	String filecontent = request.getAttribute("filecontent")==null?"":request.getAttribute("filecontent").toString().trim();
	out.println(filecontent);
%>