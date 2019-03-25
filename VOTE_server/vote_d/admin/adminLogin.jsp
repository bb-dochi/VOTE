<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<head>
<title>login page</title>
<STYLE type="text/css">
.t1 {background-color: red; border-color:transparent;color:#ffffff;}
</STYLE>
</head>
<body>

<form name="loginform" method="post" action="AdminloginAccess.jsp">
<div>
<img src="imglogo.jpg" style="margin-left: auto; margin-right: auto; display: block;" width="166px" height="47px"/>
	<div>
		<div><input class="logininput" type="text" name="id" value="ID" onFocus="value=''" style="margin-left: auto; margin-right: auto; display: block;width:150px; height:15px;margin-bottom: 7px"> </div>
	</div>
	<div>
		<div><input class="logininput" type="password" name="pw" value="PASSWORD" onFocus="value=''" style="margin-left: auto; margin-right: auto; display: block;width:150px; height:15px; margin-bottom: 7px"></div>
	</div>
	<div>
		<div><input class="t1" type="submit" name="login1" value="LOGIN" style="margin-left: auto; margin-right: auto; display: block;width:157px; height:21px;" ></div>
	</div>
</div>
</form>

</body>
</html>
