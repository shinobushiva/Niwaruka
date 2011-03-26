<%@page pageEncoding="UTF-8" isELIgnored="false" session="false"%>

<nav>
<ul class="tabs">
	<li class="tab"><a class="tab-text" href="/niwaruka/member" title="<fmt:message key='msg.home' />"
		href="/niwaruka/member/" target="_self"><fmt:message key='msg.home' /></a></li>
	<li class="tab"><a class="tab-text" href="/niwaruka/member/mypage"
		title="<fmt:message key='msg.mypage' />" target="_self"><fmt:message key='msg.mypage' /></a></li>
	<li class="tab"><a class="tab-text" href="/niwaruka/member/tagManager"
		title="<fmt:message key='msg.tagmanager' />" target="_self"><fmt:message key='msg.tagmanager' /></a></li>
	<li class="tab"><a class="tab-text" href="/niwaruka/member/tagMailer"
		title="<fmt:message key='msg.tagmailer' />" target="_self"><fmt:message key='msg.tagmailer' /></a></li>
</ul>
<form class="logout" action="/niwaruka/login/logout">
<input class="logout-button" type="submit" value="<fmt:message key='msg.logout' />">
</form>
</nav>