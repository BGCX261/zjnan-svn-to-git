<%@ page import="com.zjnan.app.security.spring.SpringSecurityUtils" %>
<%@ include file="/common/taglibs.jsp"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<title>user manager</title>
	<link href="${ctx}/css/default.css" type="text/css" rel="stylesheet" />
    <script src="${ctx}/js/jquery.form.js" type="text/javascript"></script>
	<script  src="<c:url value='/scripts/table.js'/>" type="text/javascript"></script>
</head>

<body>
<div id="menu">
	<h3>
	<a href="<c:url value='/user/users.html'/>">user list</a> &nbsp;||
	<security:authorize ifAnyGranted="ROLE_ADMIN">
        <a href="editUser.html">add user</a>
    </security:authorize>
	</h3> 
</div>

<div id="message"><s:actionmessage /></div>

<form id="mainForm" action="users.html" method="get">

<div id="filter">
	Hello,<%=SpringSecurityUtils.getCurrentUserName()%>.&nbsp;&nbsp;
	<br>
	<br>
	<br>
 	login name: 
 	<input type="text" name="filter_EQ_firstName" value="${param['filter_EQ_firstName']}"  size="9"/> 
    username or Email: 
    <input type="text" name="filter_LIKE_email" value="${param['filter_LIKE_email']}" size="9"/>
	<input type="submit" value="search" />
</div> 


<div id="listContent">
	<table>
		<tr>
			<th><a href="javascript:sort('loginName','asc')"><b>login name</b></a></th>
			<th><a href="javascript:sort('name','asc')""><b>name</b></a></th>
			<th><a href="javascript:sort('email','asc')"><b>Email</b></a></th>
			<th><b>role</b></th>
			<th><b>operating</b></th>
		</tr>
	
		<s:iterator value="page.result">
			<tr>
				<td>${firstName}&nbsp;</td>
				<td>${username}&nbsp;</td>
				<td>${email}&nbsp;</td>
				<td>${roleNames}&nbsp;</td>
				<td>&nbsp; 
					<security:authorize ifAnyGranted="ROLE_ADMIN">
						<a href="${id}" onclick="edit('${id}','editUser.html',this);return false;">mod</a>&nbsp;
						<a href="${id}" onclick="edit('${id}','deleteUser.html',this);return false;">delete</a>
						<a href="removeUser.html?id=${id}" onclick="remove(this);return false;">remove</a>
					</security:authorize>
				</td>
			</tr>
		</s:iterator>
	</table>
</div>

<%@ include file="/common/pagination.jsp" %>

</form>
</body>
</html>
