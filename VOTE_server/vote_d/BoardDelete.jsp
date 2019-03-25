<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="vote.*" %>
<%@ page import="java.util.*" %>
<%@ page import="org.json.simple.*"%>
<jsp:useBean id="dao" class="vote.DAO"/>
<%
try{
request.setCharacterEncoding("UTF-8");

int result =0;
if(request.getParameter("idx")!=null){
	int idx=Integer.parseInt(request.getParameter("idx"));
	result = dao.deleteBoard(idx);
}
if(result==1)
	out.print("성공");
else
	out.print("실패");
}catch(Exception e){out.print(e);}

%>
