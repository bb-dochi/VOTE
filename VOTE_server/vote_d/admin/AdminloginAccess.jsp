<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="java.util.*, vote.*"%>
<!DOCTYPE html>
<html>
<head>

<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
<jsp:useBean id="cusdao" class="vote.CusDAO" scope="page" />

<%
	String id= request.getParameter("id");
	String pw= request.getParameter("pw");
	boolean adminCheck = cusdao.AdminCheck(id,pw);
	
	if(adminCheck){
		session.setAttribute("loginad", id);
		response.sendRedirect("admin.jsp");
	}
	else{%>
		<script>
		alert("로그인 실패하셨습니다.");
		location.href="/vote_d/admin/adminLogin.jsp";
		</script>
		
	<% 
}
		
%>
</body>
</html>
