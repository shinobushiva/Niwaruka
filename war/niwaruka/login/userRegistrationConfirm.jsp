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

<jsp:include page="/shared/header.jsp">
	<jsp:param value='<div class="title">新規アカウント登録 確認</div>' name='name' />
</jsp:include>


<section>

<article>
<div class="message">以下の登録内容でよろしいですか？</div>
</article>

<article class="user-registration-form-article">
<form action="userRegistration" method="post">
<div class="registration-form">
<table>
	<tr>
		<td><span class="field_name">にわるか ID</span></td>
		<td><span class="field_value"><em>${u.name}</em></span></td>
	</tr>
	<tr>
		<td><span class="field_name">お名前</span></td>
		<td><span class="field_value"><em>${u.userName}</em></span></td>
	</tr>
	<tr>
		<td><span class="field_name">メールアドレス</span></td>
		<td><span class="field_value"><em>${u.email}</em></span></td>
	</tr>
	<%--
	<tr>
		<td><span class="field_name">タグ</span></td>
		<td><span class="field_value"><em>${u.tags}</em></span></td>
	</tr> 
	--%>
</table>
</div>
<input class="button" type="submit" value="登録"><input
	class="button back" type="button" value="戻る" onClick="history.back()"></form>
</article>
</section>

<jsp:include page="/shared/footer.jsp" />

</body>
</html>
