<%@page pageEncoding="UTF-8" isELIgnored="false" session="false"%>

<header>
<div>
<a href="/niwaruka/member" title="にわるか" target="_self">
<img src="/resources/logo.png" alt="にわるか" width="391" height="73" /></a>
<a href="/niwaruka/niwarukashoukai.html" title="にわ・るか" target="_self">
<img src="/resources/niwaruka.png" alt="にわるか" height="55" style="margin-left: 10px; padding-bottom: 10px;" /></a>
</div>

<%
	if (request.getParameter("name") != null) {
%>
<div class="title"><%=request.getParameter("name")%></div>
<%
	}
%>
</header>