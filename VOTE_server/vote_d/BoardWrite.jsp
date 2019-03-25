<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="vote.*" %>
<%@ page import="java.util.*" %>

<%@page import="com.oreilly.servlet.multipart.DefaultFileRenamePolicy"%>
<%@page import="com.oreilly.servlet.MultipartRequest"%>
<jsp:useBean id="dao" class="vote.DAO"/>
<%
request.setCharacterEncoding("UTF-8");
try{
//파일 저장할 디렉토리 설정
String dir = application.getRealPath("/vote_d/img/");
int max = 10*1024*1024;

MultipartRequest mr = new MultipartRequest(request, dir, max, "UTF-8");
String id = mr.getParameter("id").trim();

Enumeration e =mr.getFileNames();
String[] img=new String[2];

int i=1;
while(e.hasMoreElements())
{
	String ImgName = (String)e.nextElement();
	String filename = mr.getFilesystemName(ImgName);
	String original = mr.getOriginalFileName(ImgName);
	img[i--]=original;
}

out.print(img[0]+"이미지가 저장되었습니다.");
out.print(img[1]+"이미지가 저장되었습니다.");

String category = dao.pasing(mr.getParameter("category").toString().trim());

//String title = new String( mr.getParameter("title").getBytes("8859_1"),"utf-8");
String title = java.net.URLDecoder.decode(mr.getParameter("title"),"UTF-8");

//String memo = dao.pasing(mr.getParameter("memo").toString().trim());
String memo = java.net.URLDecoder.decode(mr.getParameter("memo"),"UTF-8");

String list1 = dao.pasing(mr.getParameter("list1").toString().trim());
String list2 = dao.pasing(mr.getParameter("list2").toString().trim());
int noname =0;
if((mr.getParameter("noname"))!=null){
	noname = Integer.parseInt(mr.getParameter("noname").trim());
}
String date = mr.getParameter("alarm").toString();
out.print("/"+category+"/"+id+"/"+title+"/"+memo+"/"+list1+"/"+list2+"/"+noname+"/"+date);
String result = dao.insertBoard(category,id,title,memo,list1,list2,img[0],img[1],noname,date);//날짜도 나중에 넣어줄 것
out.print(result);
}catch(Exception e){out.print(e); out.print("에러..");}


%>
