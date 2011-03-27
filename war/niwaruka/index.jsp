<%@page pageEncoding="UTF-8" isELIgnored="false" session="false"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@taglib prefix="f" uri="http://www.slim3.org/functions"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt"%>

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
<link rel="stylesheet" href="/css/login.css" />

<%@ include file="/shared/i18n.jsp"%>
</head>

<body> 
<jsp:include page="/shared/header.jsp" >
	<jsp:param value='<div class="title">にわるかへようこそ</div>' name='name' />
</jsp:include>

<section id="container">
<article id="shinki">
<span>にわるかは同じタグを持っている人達の間でつぶやきを共有できるサービスです。</span>
<form action="login/userRegistrationForm" method="post">
	<input class="shinki-button" type="submit" value="新規アカウント登録"></form>
	<p style="margin-left: 10px;"><a href="introduction" title="にわるかとは？" target="_self" style="color: #E06; text-decoration: underline;">にわるかとは？</a></p>
</article>

<article id="login">
<form action="login/login" method="post">
<table>
	<tr>
		<th colspan="2">ログインはこちら</th>
	</tr>
	<tr>
		<td><span class="field_name">にわるか ID</span></td>
		<td><span class="field_value"><input type="text"
			name="name"></span></td>
	</tr>
	<tr>
		<td><span class="field_name">パスワード</span></td>
		<td><span class="field_value"><input type="password"
			name="passwd"></span></td>
	</tr>
	<tr>
		<td colspan="2">ログイン状態を保持する<input type="checkbox"
			name="keepLogin" /></td>
	</tr>
	<tr>
		<td colspan="2"><input class="login-button" type="submit" value="ログイン" /></td>
	</tr>
	<tr>
		<td colspan="2"><span class="important">*</span><a
			href="login/passwordResetForm">IDやパスワードを忘れた方はこちら</a></td>
	</tr>
</table>
</form>
</article>



<%--
<article class="shinki">
<h3>新規登録</h3>
<form action="login/userRegistration" method="post">
<div><span class="field_name">ID</span><input type="text"
	name="name"></div>
<div><span class="field_name">Password</span><input
	type="password" name="passwd1"></div>
<div><span class="field_name">Password(確認)</span><input
	type="password" name="passwd2"></div>
<div><span class="field_name">メールアドレス</span><input type="text"
	name="email" size="50"></div>
<div><span class="field_name">タグ</span><input type="text"
	name="tags" size="50"></div>
<input type="submit" value="登録"></form>
 --%>
 </section>

<jsp:include page="/shared/footer.jsp" />

</body>
</html>
