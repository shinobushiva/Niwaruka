<%@page pageEncoding="UTF-8" isELIgnored="false" session="false"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@taglib prefix="f" uri="http://www.slim3.org/functions"%>

<!DOCTYPE html>
<html lang="ja">
<head>
<meta charset="utf-8">
<title>にわるか</title>
<!-- The below script Makes IE understand the new html5 tags are there and applies our CSS to it -->
<!--[if IE]>
<script src="http://html5shiv.googlecode.com/svn/trunk/html5.js"></script>
<![endif]-->
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta name="Content-Style-Type" content="text/css" />
<script src="/js/jquery-1.4.3.js" type="text/javascript"></script>
<script src="/js/password_strength_plugin.js" type="text/javascript"></script>
<link rel="stylesheet" href="/css/reset.css" />
<link rel="stylesheet" href="/css/global.css" />
<link rel="stylesheet" href="/css/registrationForm.css" />

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

<jsp:include page="/shared/header.jsp" >
	<jsp:param value='<div class="title">パスワード設定</div>' name='name' />
</jsp:include>


<section>
<article>

<div class="message">新しいパスワードを入力してください。</div>

<div class="error">
<ul>
	<c:forEach var="e" items="${f:errors()}">
		<li><span class="field_name"><span class="important">*</span>${f:h(e)}</span></li>
	</c:forEach>
</ul>
</div>
</article>
<article class="user-registration-form-article">
<form action="newPassword" method="post"><input type="hidden"
	name="name" value="${passwordRenewal}" id="user_id">
<div class="registration-form">
<table>
	<tr>
		<td><span class="field_name"><span class="important">*</span>パスワード</span></td>
		<td><span class="field_value"><input type="password"
			name="passwd1" class="password_adv" size="50"></span></td>
	</tr>
	<tr>
		<td><span class="field_name"><span class="important">*</span>パスワード	(確認)</span></td>
		<td><span class="field_value"><input type="password" name="passwd2" size="50"></span></td>
	</tr>
</table>
</div>
<input class="button" type="submit" value="パスワード変更"></form>

</article>
</section>

<jsp:include page="/shared/footer.jsp" />

</body>
</html>
