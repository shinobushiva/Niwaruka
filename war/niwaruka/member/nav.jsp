<%@page pageEncoding="UTF-8" isELIgnored="false" session="false"%>

<nav>
<ul class="tabs">
	<li class="tab"><a class="tab-text" href="/niwaruka/member" title="ほーむ"
		href="/niwaruka/member/" target="_self">ほーむ</a></li>
	<li class="tab"><a class="tab-text" href="/niwaruka/member/mypage"
		title="まいぺーじ" target="_self">まいぺーじ</a></li>
	<li class="tab"><a class="tab-text" href="/niwaruka/member/tagManager"
		title="たぐまねーじゃ" target="_self">たぐまねーじゃ</a></li>
	<li class="tab"><a class="tab-text" href="/niwaruka/member/tagMailer"
		title="めーらー" target="_self">めーらー</a></li>
</ul>
<form class="logout" action="/niwaruka/login/logout">
<input class="logout-button" type="submit" value="ろぐあうと">
</form>
</nav>