<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="vote.*" %>
<%@ page import="java.util.*" %>

<%@page import="com.oreilly.servlet.multipart.DefaultFileRenamePolicy"%>
<%@page import="com.oreilly.servlet.MultipartRequest"%>
<jsp:useBean id="dao" class="vote.DAO"/>
<%
request.setCharacterEncoding("UTF-8");
int index = Integer.parseInt(request.getParameter("boardIndex"));
String id = request.getParameter("id");
int checkNum = Integer.parseInt(request.getParameter("CheckNum"));

int isVote = dao.IsVote(index,id);
if(isVote==0)
{
	String result =	dao.VoteNow(index,id,checkNum);
	out.print(result);
}else if(isVote==1){
	out.print("이미 투표하셨습니다");
}else{
	out.print("에러");
}
%>
