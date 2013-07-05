<%@page import="java.util.ArrayList"%>
<%
	String filepath = request.getAttribute("filepath")==null?"":request.getAttribute("filepath").toString().trim();
	ArrayList al = (ArrayList)request.getAttribute("resultSet");
System.out.println("***********"+al);
%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>生成文件树</title>
<style>
.title {
     border:0px;
     font-size:20px;
}
.node {
     padding-left:30px;
     overflow:hidden;
}
.icon {
     display:inline;
     margin-right:5px;
     cursor:pointer;
}
</style>
<script language="javascript">

<%if(al!=null){%>
window.onload = function(){
<%
for(int i=0;i<al.size();i++){
	ArrayList tmpList = (ArrayList)al.get(i);
	String parentId = tmpList.get(0)==null?"":tmpList.get(0).toString();
	String nodeId = tmpList.get(1)==null?"":tmpList.get(1).toString();
	String nodeName = tmpList.get(2)==null?"":tmpList.get(2).toString();
	String fileType = tmpList.get(3)==null?"":tmpList.get(3).toString();
	System.out.println(tmpList);
%>
	var parentId = "<%=parentId%>";
	var nodeId = "<%=nodeId%>";
	var nodeName = "<%=nodeName%>";
	var fileType = "<%=fileType%>"
	var oParent = document.getElementById(parentId);
    //父节点合法不？
    if(oParent == null){
    	return;
    }
    var oNode = document.createElement('div');
    oNode.id = nodeId;
    oNode.className = 'node';
    //真够费劲的
    if(fileType=="folder"){
	    oNode.innerHTML = "<div id='"+nodeId+"_title' class='title'><span class='icon' onclick='fold(this)'><img src=\"images/folder-open.gif\" /></span>"+nodeName+"</div>";
    }else if(fileType=="file"){
	    oNode.innerHTML = "<div id='"+nodeId+"_title' class='title'><span class='icon' onclick='fold(this)'><img src=\"images/leaf.gif\" /></span>"+nodeName+"</div>";
    }
    oParent.appendChild(oNode);
<%}%>
}
<%}%>
function showPath(){
	document.frmLst.action = "genFile";
	document.frmLst.submit();
}

function fold(element)
{
	if(element.innerHTML.indexOf("images/folder.gif") !=-1)
	  {
		   element.parentNode.parentNode.style.height = '';
		   element.innerHTML = '<span ><img src="images/folder-open.gif" /></span>';
	  }
	  else if(element.innerHTML.indexOf("images/folder-open.gif") !=-1)
	  {
		   element.innerHTML = '<span ><img src="images/folder.gif" /></span>';
		   element.parentNode.parentNode.style.height = element.parentNode.offsetHeight;
	  }
}
</script>
</head>
<body>
<form name="frmLst">
文件路径：<input type="text" name="filepath" id="filepath" size="36" value="<%=filepath%>"><input type="button" value="提交" onClick="showPath()">
<div id="root"></div>
</form>
</body>
</html>