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
<link rel="stylesheet" href="/css/tag.css" />
<link rel="stylesheet" href="/css/login.css" />
<link rel="stylesheet" href="/css/index.css" />

<script src="/js/jquery-1.4.3.js" type="text/javascript"></script>
<script src="/js/jquery.form.js" type="text/javascript"></script>
<script src="/js/jquery.timer.js" type="text/javascript"></script>
<script src="/js/niwaruka.utils.js" type="text/javascript"></script>
<script src="/js/niwaruka.tag.js" type="text/javascript"></script>

<script type="text/javascript">
	$(document).ready(function() {
		getTweets(${tag.key.id});
	});
</script>
</head>

<body> 
<jsp:include page="/shared/header.jsp" />

<c:if test="${user != null}">
	<jsp:include page="/niwaruka/member/nav.jsp" />
</c:if>

<section>
<article>
<script type="text/javascript">
	// wait for the DOM to be loaded 
	$(document).ready(function() {
		var label = $("#tag");
		if ("${tag.tag}".indexOf("#") == 0) {
			label.attr("class", "tag-toggled twitter");
		} else if ("${tag.access}".indexOf("sharedTag") != -1) {
			label.attr("class", "tag-toggled shared");
		} else if ("${tag.access}".indexOf("secretTag") != -1) {
			label.attr("class", "tag-toggled secret");
		} else {
			label.attr("class", "tag-toggled public");
		}
	});
</script>
<div class="tag-title"><span id="tag">${tag.tag}</span><span
	class="tag-title right">現在、${tag.userCount}人がこのタグを使用しています。</span></div>
<c:if test="${user != null}">
	<c:choose>
		<c:when
			test="${!fn:contains(user.tags, tag.key) && tag.requirePermission && user.key != tag.creatorRef.key}">
			<form id="follow" action="/niwaruka/member/tagFollow">
			<div>このタグのオーナーは<span class="name">${tag.creatorRef.model.userName}</span><span
				class="id">${tag.creatorRef.model.name}</span>さんです</div>
			<div><input type="hidden" name="id" value="${tag.key.id}"></div>
			<div><input type="submit" value="フォローをリクエストする" /></div>
			</form>
		</c:when>
		<c:when test="${!fn:contains(user.tags, tag.key)}">
			<form id="follow" action="/niwaruka/member/tagFollow"><c:if
				test="${tag.creatorRef.key != null}">
				<div>このタグのオーナーは<span class="name">${tag.creatorRef.model.userName}</span><span
					class="id">${tag.creatorRef.model.name}</span>さんです</div>
			</c:if>
			<div><input type="hidden" name="id" value="${tag.key.id}"></div>
			<div><input type="submit" value="フォローする" /></div>
			</form>
		</c:when>
		<c:when test='{!fn:startsWith(tag.tag,"#")}'>
			<form id="follow" action="/niwaruka/member/tagFollow">
			<div><a target='_blank'
				href='http://twitter.com/search/%23${tag.tag}'>Twitter
			${tag.tag}</a></div>
			<div><input type="hidden" name="id" value="${tag.key.id}"></div>
			<div><input type="submit" value="フォローをリクエストする" /></div>
			</form>
		</c:when>
		<c:otherwise>
			<form id="follow" action="/niwaruka/member/tagFollow"><c:if
				test="${tag.creatorRef.key != null}">
				<div>このタグのオーナーは<span class="name">${tag.creatorRef.model.userName}</span><span
					class="id">${tag.creatorRef.model.name}</span>さんです</div>
			</c:if>
			<div><input type="hidden" name="id" value="${tag.key.id}"></div>
			<div><input type="submit" value="フォローをやめる" /></div>
			</form>
		</c:otherwise>
	</c:choose>
</c:if> <c:if test="${user == null}">
	<div class="shinki"><span>にわるかは同じタグを持っている人達の間でつぶやきを共有できるサービスです。</span><br />
	<span>登録は5分で完了して無料で使えます。</span>
	<form action="/" method="post"><input class="shinki-button"
		type="submit" value="今すぐ「にわるか」を始める"></form>
	</div>
</c:if>

<%-- ここから --%>

<article>
<hr />

<div id="new-tweets-bar" class="new-tweets-bar hidden"
	onclick="getTweets(${tag.key.id});"><span></span></div>
<p id="message"></p>
<div id="older-tweets-bar" class="new-tweets-bar hidden"
	onclick="getOlderTweets(${tag.key.id});"><span>もっと読む</span></div>
	
</article>

</article>
</section>

<jsp:include page="/shared/footer.jsp" />
</html>
