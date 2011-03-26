<%@page pageEncoding="UTF-8" isELIgnored="false" session="false"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@taglib prefix="f" uri="http://www.slim3.org/functions"%>

<!-- /niwaruka/index.jsp -->
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
	<jsp:param value='<div class="title">エラーが発生しました</div>' name='name' />
</jsp:include>


<section>
<article>
<div class="message">ご迷惑をおかけしております。</div>
</article>
<article>
<form action="/" method="post"><input class="button back-to-home"
	type="submit" value="ログイン画面に戻る"></form>
</article>
</section>

<jsp:include page="/shared/footer.jsp" />

</body>
</html>
