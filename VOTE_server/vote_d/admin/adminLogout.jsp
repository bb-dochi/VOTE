<%@ page contentType="text/html; charset=UTF-8" %>
<%
		session.removeAttribute("loginad");
%>
<script>
    alert("로그아웃 되었습니다.");
	location.href="adminLogin.jsp";
</script>
