<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" import="vote.*"%>
   <%@ page import="java.lang.*" %>
<jsp:useBean id="dao" class="vote.DAO"/>

<% request.setCharacterEncoding("UTF-8");

	int idx = Integer.parseInt(request.getParameter("idx"));
 %>
<!DOCTYPE html>
<html>
 <head>
 <title>게시판</title>
 </head>
 <body>
<%
 	String loginName=(String)session.getAttribute("loginad");
 	boolean login= loginName==null ? false:true;
 	if(login){%>
 <section class="centered"> 
	<% 
	String loginad=(String)session.getAttribute("loginad");
	int result = dao.deleteBoard(idx);
	if(result==1){
	%><script>
		alert("삭제가 완료되었습니다.");
		location.href="/vote_d/admin/admin.jsp";
		</script>
	<%}%>
 </section>

<% }else{ %>
	<script>
	alert("로그인이 필요합니다.");
	location.href="adminLogin.jsp";
	</script>	
<%}%>
</body> 
</html>

