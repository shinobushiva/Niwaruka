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
<link rel="stylesheet" href="/css/reset.css" />
<link rel="stylesheet" href="/css/global.css" />
<link rel="stylesheet" href="/css/registrationForm.css" />
</head>

<body>

<jsp:include page="/shared/header.jsp" >
	<jsp:param value='<div class="title">パスワードリセット</div>' name='name' />
</jsp:include>


<section>
<article>
<div class="message">パスワードをリセットします。</div>
<div class="message">にわるかID、又は、ご登録のメールアドレスを入力して送信をクリックしてください。</div>
<div class="error">
<ul>
	<c:forEach var="e" items="${f:errors()}">
		<li><span class="field_name"><span class="important">*</span>${f:h(e)}</span></li>
	</c:forEach>
</ul>
</div>
</article>

<article class="user-registration-form-article">
<form action="passwordReset" method="post">
<div class="registration-form">
<table>
	<tr>
		<td><span class="field_name">にわるか ID</span></td>
		<td><span class="field_value"><input id="user_id"
			type="text" name="name"></span></td>
	</tr>
	<tr>
		<td><span class="field_name">メールアドレス</span></td>
		<td><span class="field_value"><input type="text"
			name="email" size="50"></span></td>
	</tr>
</table>
</div>
<input class="button" type="submit" value="送信"><input
	class="button back" type="button" value="戻る" onClick="history.back()"></form>
</article>
</section>

<jsp:include page="/shared/footer.jsp" />

</body>
</html>