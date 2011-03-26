<%@page pageEncoding="UTF-8" isELIgnored="false" session="false"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@taglib prefix="f" uri="http://www.slim3.org/functions"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt"%>


<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<title><fmt:message key='msg.mypage' /></title>
<!-- The below script Makes IE understand the new html5 tags are there and applies our CSS to it -->
<!--[if IE]>
<script src="http://html5shiv.googlecode.com/svn/trunk/html5.js"></script>
<![endif]-->
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta name="Content-Style-Type" content="text/css" />
<script src="/js/jquery-1.4.3.js" type="text/javascript"></script>
<script src="/js/password_strength_plugin.js" type="text/javascript"></script>

<%@ include file="/shared/i18n.jsp"%>

<link rel="stylesheet" href="/css/reset.css" />
<link rel="stylesheet" href="/css/global.css" />
<link rel="stylesheet" href="/css/mypage.css" />
<script type="text/javascript">
	$(document).ready(function() {
		$(".password_adv").passStrength({
			shortPass : "top_shortPass", //optional
			badPass : "top_badPass", //optional
			goodPass : "top_goodPass", //optional
			strongPass : "top_strongPass", //optional
			baseStyle : "top_testresult", //optional
			userid : "#user_id", //required override
			messageloc : 0
		//before == 0 or after == 1
		});
	});
</script>
</head>
<body>
<jsp:include page="/shared/header.jsp" flush="true">
	<jsp:param value="<fmt:message key='msg.mypage' />" name="name" />
</jsp:include>
<%@ include file="/niwaruka/member/nav.jsp"%>

<section>
<article class="mypage">
<input type="hidden" id="user_id" value="${f:h(user.name)}" />
<div class="twitter-connect">ユーザID : ${f:h(user.name)}</div>

<%--ユーザ情報設定 --%>
<div class="error">
<ul>
	<c:forEach var="e" items="${f:errors()}">
		<li><span class="field_name"><span class="important">*</span>${f:h(e)}</span></li>
	</c:forEach>
</ul>
<span class="error">${message}</span></div>
</article>


<article>
<form method="post" action="/niwaruka/member/mypageConfig"><input type="hidden" name="name" value="${user.name}" />
<div class="registration-form">
<table>
	<tr>
		<td><span class="field_name"><span class="important">*</span>パスワード</span></td>
		<td><span class="field_value"><input type="password" name="passwd1" class="password_adv" value="${user.passwd1}"></span></td>
	</tr>
	<tr>
		<td><span class="field_name"><span class="important">*</span>パスワード	(確認)</span></td>
		<td><span class="field_value"><input type="password" name="passwd2" value="${user.passwd2}"></span></td>
	</tr>
	<tr>
		<td><span class="field_name"><span class="important">*</span>お名前</span></td>
		<td><span class="field_value"><input type="text" name="userName" value="${user.userName}"></span></td>
	</tr>
	<tr>
		<td><span class="field_name"><span class="important">*</span>メールアドレス</span></td>
		<td><span class="field_value"><input type="text" name="email" size="50" value="${user.email}"></span></td>
	</tr>
	<tr>
		<td><span class="field_name">メールアドレス(携帯など)</span></td>
		<td><span class="field_value"><input type="text" name="email2" size="50" value="${user.email2}"></span></td>
	</tr>
	<%--
	<tr>
		<td colspan="2">
		<hr />
		あなたが興味のある言葉を「 , 」(コンマ)で区切って並べてください</td>
	</tr>
	<tr>
		<td><span class="field_name">タグ</span></td>
		<td><span class="field_value"><input type="text" name="tags" size="50" value="${user.tags}"></span></td>
	</tr>
	 --%>
</table>
</div>
<input class="button" type="submit" value="変更"></form>
</article>

<article>
<%-- 画像アップロード --%>
<div class="twitter-connect"><img
	src="/niwaruka/userImage?name=${user.name}" />
<form action="upload" method="post" enctype="multipart/form-data">
<input type="file" name="formFile" /> <input type="submit"
	value="アップロード" /></form>
<form action="upload" method="post"><input type="hidden"
	name="clear" value="true" /> <input type="submit" value="デフォルトに戻す" /></form>
</div>

<%--ツイッター連携 --%> <c:if test="${twitterId == null}">
	<form action="extsrv/twitter/set" method="post" target="_top">
	<div class="twitter-connect">Twitterと連携しますか !!!? <input
		type="submit" value="連携" /></div>
	</form>
</c:if> <c:if test="${twitterId != null}">
	<form action="extsrv/twitter/unset" method="post">
	<div class="twitter-connect">Twitterとの連携を解除しますか !!!? Twitter ID :
	<b>${f:h(twitterId)}</b> <input type="submit" value="解除" /></div>
	</form>
</c:if></article>
</section>

	<%@ include file="/shared/footer.jsp"%>
</html>
