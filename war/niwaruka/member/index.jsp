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
<meta name="Content-Script-Type" content="text/javascript" />
<meta name="Content-Style-Type" content="text/css" />
<link rel="stylesheet" href="/css/reset.css" />
<link rel="stylesheet"
	href="/css/ui-lightness/jquery-ui-1.8.11.custom.css" />
<link rel="stylesheet" href="/css/global.css" />
<link rel="stylesheet" href="/css/style.css" />
<link rel="stylesheet" href="/css/index.css" />

<script src="/js/jquery-1.5.1.min.js" type="text/javascript"></script>
<script src="/js/niwaruka.utils.js" type="text/javascript"></script>
<script src="/js/niwaruka.member.index.js" type="text/javascript"></script>
<script src="/js/jquery.form.js" type="text/javascript"></script>
<script src="/js/jquery-ui-1.8.11.custom.min.js" type="text/javascript"></script>
<script src="/js/jquery.timer.js" type="text/javascript"></script>
<script src="/js/jquery.inputHintOverlay.js" type="text/javascript"></script>
<script type="text/javascript" src="/_ah/channel/jsapi"></script>

<script type="text/javascript">
	var gUserId = "${userData.key.id}";

	$(document).ready(function() {
		$('form#tweetForm').inputHintOverlay(5, 7);
	});

	$(function() {
		$('#refreshButton').click(function() {
			refresh();
		});
		refresh();
	});

	$(function() {
		$('#tweetButton').click(function() {
			tweet();
		});
	});

	$(document).ready(function() {
		// bind 'myForm' and provide a simple callback function 
		$('#tagsForm').ajaxForm(function() {
			//alert("Thank you for your comment!"); 
		});
	});

	$(document).ready(function() {
		var time = 20 * 1000;
		$.timer(time, function(timer) {
			if (shouldCheck) {
				updateCheck();
				checkRequests();
			}
			timer.reset(time);
		});
		checkRequests();
	});

	$(function() {
		$('#slideDownTab').click(function() {
			var sdc = $("#slideDownContent");
			if (sdc.is(":hidden")) {
				sdc.slideDown("normal");
			} else {
				sdc.slideUp("normal");

				if (sdc.find('li').length == 0) {
					$("#slideDown").fadeOut(2000);
				}
			}
		});
	});
</script>

<%-- タグの切り替えフォーム用Script --%>
<c:forEach var="e" items="${tags}">
	<script type="text/javascript">
		$(document).ready(function() {
			var id = "#check-${e.key.id}";
			$(id).bind("click", function() {
				var f = $("#tagsForm");
				$.ajax({
					type : 'POST',
					url : f.attr('action'),
					data : f.serialize(),
					success : function(json) {
						var label = $(id + "-label");
						label.toggleClass("tag-toggled");
						label.toggleClass("tag-untoggled");
						refresh();
					}
				});
			});
		});
	</script>
</c:forEach>

</head>
<body>
	<jsp:include page="/shared/header.jsp">
		<jsp:param value="ほーむ" name="name" />
	</jsp:include>
	<jsp:include page="/niwaruka/member/nav.jsp" />

	<%--　通知ボックス --%>
	<div id="slideDown">
		<div id="slideDownContentDummy"></div>
		<div id="slideDownContent">
			<span id="slideDownContentBody"></span>
		</div>
		<div id="slideDownTab">
			<img src="/resources/niwaruka.png" /><span>おしらせ</span>
		</div>

	</div>

	<section>
		<article>
			<div class="nanishite-niwaruka">
				今何して<b>にわるか</b>？<span id="loading-message"
					class="loading-message hidden">loading...</span>
			</div>

			<%--タグの切り替えフォーム --%>
			<div class="tag-list">
				<form id="tagsForm" method="post" action="tagChangeJson">
					<c:forEach var="e" items="${enabledTags}">
						<input class="hidden-checkbox" id="check-${f:h(e.key.id)}"
							type="checkbox" checked="checked" ${f:multibox("tagArray", f:h(e.key.id))}/>
						<c:choose>
							<c:when test='${fn:startsWith(f:h(e.tag),"#")}'>
								<label for="check-${f:h(e.key.id)}"
									id="check-${f:h(e.key.id)}-label" class="tag-toggled twitter">${f:h(e.tag)}</label>
							</c:when>
							<c:when test='${fn:startsWith(f:h(e.access),"sharedTag")}'>
								<label for="check-${f:h(e.key.id)}"
									id="check-${f:h(e.key.id)}-label" class="tag-toggled shared">${f:h(e.tag)}</label>
							</c:when>
							<c:when test='${fn:startsWith(f:h(e.access),"secretTag")}'>
								<label for="check-${f:h(e.key.id)}"
									id="check-${f:h(e.key.id)}-label" class="tag-toggled secret">${f:h(e.tag)}</label>
							</c:when>
							<c:otherwise>
								<label for="check-${f:h(e.key.id)}"
									id="check-${f:h(e.key.id)}-label" class="tag-toggled public">${f:h(e.tag)}</label>
							</c:otherwise>
						</c:choose>
					</c:forEach>
					<c:forEach var="e" items="${disabledTags}">
						<input class="hidden-checkbox" id="check-${f:h(e.key.id)}"
							type="checkbox" ${f:multibox("tagArray", f:h(e.key.id))}/>
						<c:choose>
							<c:when test='${fn:startsWith(f:h(e.tag),"#")}'>
								<label for="check-${f:h(e.key.id)}"
									id="check-${f:h(e.key.id)}-label" class="tag-untoggled twitter">${f:h(e.tag)}</label>
							</c:when>
							<c:when test='${fn:startsWith(f:h(e.access),"sharedTag")}'>
								<label for="check-${f:h(e.key.id)}"
									id="check-${f:h(e.key.id)}-label" class="tag-untoggled shared">${f:h(e.tag)}</label>
							</c:when>
							<c:when test='${fn:startsWith(f:h(e.access),"secretTag")}'>
								<label for="check-${f:h(e.key.id)}"
									id="check-${f:h(e.key.id)}-label" class="tag-untoggled secret">${f:h(e.tag)}</label>
							</c:when>
							<c:otherwise>
								<label for="check-${f:h(e.key.id)}"
									id="check-${f:h(e.key.id)}-label" class="tag-untoggled public">${f:h(e.tag)}</label>
							</c:otherwise>
						</c:choose>
					</c:forEach>
				</form>
			</div>
			<%-- 発言ボックス --%>
			<div class="tweet-box-div">
				<form id="tweetForm" action="tweet" method="post">
					<textarea title="いまなにしてる？" class="tweet-box" name="tweet"></textarea>
					<input id="tweetButton" type="button" value="にわるかる" />
				</form>
			</div>
		</article>

		<%--発言リスト及び、新規、過去の発言取得ボタン --%>
		<article>
			<hr />
			<div id="new-tweets-bar" class="new-tweets-bar hidden"
				onclick="refresh();">
				<span></span>
			</div>
			<p id="message"></p>
			<div id="older-tweets-bar" class="new-tweets-bar hidden"
				onclick="fetchOlderTweets();">
				<span>もっと読む</span>
			</div>
		</article>
	</section>

	<jsp:include page="/shared/footer.jsp" />
</body>
</html>
