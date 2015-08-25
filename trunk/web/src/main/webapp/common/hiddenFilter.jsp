<%@ page contentType="text/html;charset=UTF-8"%>
<%@page import="com.zjnan.app.webapp.util.WebUtils"%>
<% String filters = request.getParameter("filter");
   if ( filters == null || filters.length() == 0) filters="filter_,page"; %>
<%=WebUtils.buildHiddernFilters(request, filters)%>