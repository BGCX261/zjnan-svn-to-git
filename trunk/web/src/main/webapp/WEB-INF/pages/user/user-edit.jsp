<%@ include file="/common/taglibs.jsp"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<title>user manager </title>
	<%@ include file="/common/meta.jsp"%>
	<link href="${ctx}/css/default.css" type="text/css" rel="stylesheet" />
	<link href="<c:url value='/scripts/validate/jquery.validate.css'/>" type="text/css" rel="stylesheet" />
	<script src="<c:url value='/scripts/validate/jquery.validate.js'/>" type="text/javascript"></script>
	<script src="<c:url value='/scripts/validate/messages_cn.js'/>" type="text/javascript"></script>
	<script>
		
     function back(act, _this){
     form = $(_this).parents('form');
     form = form[0];
       with (form) {
           action = act ;
           submit();
       }
    }
	</script>
</head>

<body>

<div id="message"><s:actionmessage /></div>

<h3><s:if test="id == null">add</s:if><s:else>modify</s:else>user</h3>
<div id="inputContent">
<form id="inputForm" action="saveUser.html" method="post">
<input type="hidden" name="id" value="${id}" />

<c:import url="/common/hiddenFilter.jsp"/>
    
<table class="inputView">
	<tr>
		<td>login name:</td>
		<td><input type="text" name="firstName" size="40" id="firstName" value="${firstName}" /></td>
	</tr>
	<tr>
		<td>username:</td>
		<td><input type="text" name="username" id="username" size="40" value="${username}" /></td>
	</tr>
	<tr>
        <td>lastname:</td>
        <td><input type="text" name="lastName" size="40" value="${lastName}" /></td>
    </tr>
	<tr>
		<td>password:</td>
		<td><input type="password" id="password" name="password" size="40" value="${password}" /></td>
	</tr>
	<tr>
		<td>confirm password:</td>
		<td><input type="password" name="passwordConfirm" size="40" value="${password}" /></td>
	</tr>
    <tr>
        <td>AccountExpired:</td>
        <td> <s:checkbox key="accountExpired" id="accountExpired" fieldValue="true" theme="simple"/></td>
    </tr>
    <tr>
        <td>address.city:</td>
        <td><input type="text" name="address.city" size="40" value="${address.city}" /></td>
    </tr>
    <tr>
        <td>address.postalCode:</td>
        <td><input type="text" name="address.postalCode" size="40" value="${address.postalCode}" /></td>
    </tr>
	<tr>
		<td>email:</td>
		<td><input type="text" name="email" size="40" value="${email}" /></td>
	</tr>
	<tr>
		<td>role:</td>
		<td>
			<div style="word-break:break-all;width:250px; overflow:auto; ">
			    <s:if test="allRoles">
			       <s:checkboxlist name="checkedRoleIds"  list="allRoles"  listKey="id" listValue="name" theme="simple"/>
			    </s:if>
            </div>
		</td>
	</tr>
	<tr>
		<td colspan="2">
			<input type="submit" value="submit" />&nbsp; 
			<input type="button" value="cancel" onclick="back('users.html',this)"/>
		</td>
	</tr>
</table>
</form>
</div>
</body>
</html>