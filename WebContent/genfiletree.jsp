<%--
    @system  文件管理系统
    @version 0.0.1
    @lastModiDate
    @describe 生成文件树
--%>
<%--
   * 修改记录:版本号     修改日期      修改人员              修改说明
   *          0.0.1  20130708  lrntothink   点击左边树能够在右边页面显示内容
--%>

<%@page import="java.util.ArrayList"%>
<%
	String filepath = request.getAttribute("filepath")==null?"":request.getAttribute("filepath").toString().trim();
	ArrayList al = (ArrayList)request.getAttribute("resultSet");
	//System.out.println("***********"+al);
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
     white-space:nowrap;	/*不换行*/
     cursor:hand;			/*鼠标图形显示为手型*/
}
.node {
     padding-left:20px;
     overflow:hidden;
}
.mainnode {
     padding-left:0px;
}
.icon {
     display:inline;
     margin-right:5px;
     cursor:pointer;
}
.divRightMenu
{
    z-index:30000;
    text-align:left;    
    cursor: default;
    position: absolute;
    background-color:#FAFFF8;
    width:160px;
    height:auto;
    border: 1px solid #333333;
    display:none;

    filter:progid:DXImageTransform.Microsoft.Shadow(Color=#333333,Direction=120,strength=5);    
}
.divMenuItem
{
    height:17px;
    color:Black;
    font-family:宋体;
    vertical-align:middle;
    font-size:10pt;
    margin-bottom:3px;
    cursor:hand;
    padding-left:30px;
    padding-top:2px;        
}
</style>
<script language="javascript">

<%if(al!=null){%>
window.onload = function(){
	createMenu();
<%
for(int i=0;i<al.size();i++){
	ArrayList tmpList = (ArrayList)al.get(i);
	String parentId = tmpList.get(0)==null?"":tmpList.get(0).toString();
	String nodeId = tmpList.get(1)==null?"":tmpList.get(1).toString();
	String nodeName = tmpList.get(2)==null?"":tmpList.get(2).toString();
	String fileType = tmpList.get(3)==null?"":tmpList.get(3).toString();
	String filePath = tmpList.get(4)==null?"":tmpList.get(4).toString();
	filePath = filePath.replaceAll("\\\\", "/");
	//System.out.println(tmpList);
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
	    oNode.innerHTML = "<div id='"+nodeId+"_title' class='title'><span class='icon' onclick='fold(this)'><img src=\"images/folder-open.gif\" /></span><span id='"+nodeId+"_menu' onmousemove=downloadFile(this) onclick=showContent(this) value='<%=filePath%>'>"+nodeName+"</span></div>";
    }else if(fileType=="file"){
	    oNode.innerHTML = "<div id='"+nodeId+"_title' class='title'><span class='icon' onclick='fold(this)'><img src=\"images/leaf.gif\" /></span><span id='"+nodeId+"_menu' onmousemove=downloadFile(this) onclick=showContent(this) value='<%=filePath%>'>"+nodeName+"</span></div>";
    }
    oParent.appendChild(oNode);
<%}%>
}
<%}%>
function showPath(){
	document.frmLst.action = "genFile";
	document.frmLst.submit();
}
/**
 * 打开或关闭文件夹
 */
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
/**
 * 点击右键进行文件下载
 */
function downloadFile(e){
	//alert(e.id);
	//$(e.id).innerHTML = "*******";
	$(e.id).oncontextmenu = function(){
		//alert(e.id);
		//alert($(e.id));
		showMenu();
		$("openfolder").onclick = function(){
			hideMenu();
			alert($(e.id).value);
		}
		return false;
	};
}
/**
 * 显示文件内容
 */
function showContent(e){
	//window.top.mainframe.location.href = "showfilecontent.jsp?filepath="+encodeURIComponent($(e.id).value);
	window.top.mainframe.location.href = "showContent?filepath="+encodeURIComponent($(e.id).value);
}
/**
 * 显示菜单
 */
function showMenu()
{
    document.documentElement.onclick  = hideMenu;
    
    var redge=document.documentElement.clientWidth-event.clientX;
    var bedge=document.documentElement.clientHeight-event.clientY;
    
    var menu = $("divRightMenu");
    if (redge<menu.offsetWidth)
    {
        menu.style.left=document.documentElement.scrollLeft + event.clientX-menu.offsetWidth
    }
    else
    {
        menu.style.left=document.documentElement.scrollLeft + event.clientX
        menu.style.display = "block";
    }
    if (bedge<menu.offsetHeight)
    {
        menu.style.top=document.documentElement.scrollTop + event.clientY - menu.offsetHeight
    }
    else
    {
        menu.style.top = document.documentElement.scrollTop + event.clientY
		//menu.style.visibility = "visible";
        menu.style.display = "block";
    }
    return false;
}

/**
 * 隐藏右键菜单
 */
function hideMenu()
{
    $("divRightMenu").style.display="none";    
}
/**
 * 创建右键菜单
 */
function createMenu(){
	var divMenu = document.createElement("div");
	divMenu.id = "divRightMenu";
	divMenu.className = "divRightMenu";
	
	var divOpenFolder          = document.createElement("Div");
	divOpenFolder.id = "openfolder";
	divOpenFolder.className   = "divMenuItem";
	divOpenFolder.onmousemove = evtMenuOnmouseMove;
	divOpenFolder.onmouseout  = evtOnMouseOut;
	divOpenFolder.innerHTML   = "下载";
	
	divMenu.appendChild(divOpenFolder);
	
	document.body.appendChild(divMenu);
}
/**
 * 鼠标移动到右键菜单时
 */
function evtMenuOnmouseMove()
{
	this.style.backgroundColor='#8AAD77';
	this.style.paddingLeft='30px';    
}
/**
 * 鼠标移出右键菜单时
 */
function evtOnMouseOut()
{
	this.style.backgroundColor='#FAFFF8';
}
function $(gID)
{
	return document.getElementById(gID);
}

</script>
</head>
<body>
<form name="frmLst">
路径：<input type="text" name="filepath" id="filepath" size="20" value="<%=filepath%>"><br>
<input type="button" value="提交" onClick="showPath()">
<div id="root" class="mainnode"></div>
</form>
</body>
</html>