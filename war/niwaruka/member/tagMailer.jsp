<%@page pageEncoding="UTF-8" isELIgnored="false" session="false"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@taglib prefix="f" uri="http://www.slim3.org/functions"%>

<!DOCTYPE html>
<html lang="ja">
<head>
<meta charset="utf-8">
<meta name="Content-Script-Type" content="text/javascript" />
<meta name="Content-Style-Type" content="text/css" />
<link rel="stylesheet" href="/css/reset.css" />
<link rel="stylesheet" href="/css/global.css" />
<link rel="stylesheet" href="/css/tagMailer.css" />
<script src="/js/jquery-1.4.3.js" type="text/javascript"></script>
<script type="text/javascript" src="/js/jquery-ui-1.8.6.custom.min.js"></script>
<script type="text/javascript" src="/js/jquery.qtip-1.0.0-rc3.min.js"></script>
<script src="/js/niwaruka.utils.js" type="text/javascript"></script>

<%@ include file="/shared/i18n.jsp" %>

<script type="text/javascript">
$(document).ready(function() {
<c:forEach var="e" items="${tags}">
		var t = $("#tag-${e.key.id}-span");
		t.draggable({
			containment : '#tagAreas',
			revert : true,
			revertDuration : 0,
			helper : "clone",
			cursorAt : {
				top : 20,
				left : 10
			},
		});
		
		var txt = "";
		if("${e.tag}".indexOf("#") == 0){
			txt = '<span class="tagInfo">'+'ハッシュタグ'+'</span>';
		}else if("${e.creatorRef.model == null}" == "true"){
	   	    txt = '<span class="tagInfo">'+'公開タグ'+'</span>';
		}else if("${e.access}".indexOf("sharedTag")!=-1){
	   	    txt = '<span class="tagInfo">'+'共有タグ : '+'${e.creatorRef.model.name}'+'</span>';
		}else if("${e.access}".indexOf("secretTag")!=-1){
	   	    txt = '<span class="tagInfo">'+'秘密タグ : '+'${e.creatorRef.model.name}'+'</span>';
		}
		tippingWithText(t, txt, ${e.key.id});
</c:forEach>

<c:forEach var="ee" items="${tagMails}">
		var div = $("<div></div>");
	<c:forEach var="ee2" items="${ee.tagList}">
		createTag(div, "${ee2.key.id}", "${ee2.tag}", true, "${ee2.access}");
		/* var t = $("#tag-${ee2.key.id}-span");
		t.ready(function(){
		var txt = "";
		if("${ee2.tag}".indexOf("#") == 0){
			txt = '<span class="tagInfo">'+'ハッシュタグ'+'</span>';
		}else if("${ee2.creatorRef.model == null}" == "true"){
	   	    txt = '<span class="tagInfo">'+'公開タグ'+'</span>';
		}else if("${ee2.access}".indexOf("sharedTag")!=-1){
	   	    txt = '<span class="tagInfo">'+'共有タグ : '+'${ee2.creatorRef.model.name}'+'</span>';
		}else if("${ee2.access}".indexOf("secretTag")!=-1){
	   	    txt = '<span class="tagInfo">'+'秘密タグ : '+'${ee2.creatorRef.model.name}'+'</span>';
		}
		alert(txt);
		tippingWithText(t, txt, ${ee2.key.id});
		}); */
	</c:forEach>
	createTagMailBox(div, "${ee.option}", "${ee.email}");
</c:forEach>

});
</script>

<script type="text/javascript">
var formNum = 0;

var dirty = false;
function flagDirty(){
	dirty = true;
	$('#postLabel').fadeIn(1000);
	$('#postLabel').html("*保存されていません").addClass('red');
}

function unFlagDirty(){
	dirty = false;
	$('#postLabel').html("保存されました").removeClass('red');
	$('#postLabel').fadeOut(1500);
	
}

function tag(e, ui, target){
	if(target.find("span.tag").is("#"+ui.draggable.attr('id'))){
		return;
	}
	
	var sp = ui.draggable.clone();
	sp.mousedown(function(e){
		sp.remove();
		if(!target.find('span').is('.tag')){
			$('<span class="hint">タグをドロップしてください</span>').appendTo(target)
		}
		flagDirty();
	});
	sp.appendTo(target);
	
	flagDirty();
}


function toDroppable(node){
	node.droppable({
		drop : function(e, ui) {
			node.find('.hint').remove();
			tag(e,ui, node);
		}
	});
}

function createTag(d, id, t, flag, access){
	var tag = t.tag;
	if(!t.tag)
		tag = t;
	if(!access)
		access = "";
	var text = "";
	text += '<span id="tag-'+id+'-span">';
	if(tag.indexOf("#") == 0){
		text += '<span id="tag-'+id+'" class="tag twitter">'+tag+'</span>';
	}else if(access.indexOf("sharedTag") != -1){
		text += '<span id="tag-'+id+'" class="tag shared">'+tag+'</span>';
	}else if(access.indexOf("secretTag") != -1){ 
		text += '<span id="tag-'+id+'" class="tag secret">'+tag+'</span>';
	}else{
		text += '<span id="tag-'+id+'" class="tag public">'+tag+'</span>';
	}
	text += '</span>';
	
	var sp = $(text);
	sp.appendTo(d);
	
	if(!flag){
		sp.ready(function(){
		sp.draggable({
			containment : '#tagAreas',
			revert : true,
			revertDuration : 0,
			helper : "clone",
			cursorAt : {
				top : 20,
				left : 10
			},
		});
		});
		tipping(sp, t);
	}
		
	if(flag){
		sp.mousedown(function(e){
			var target = sp.parent();
			sp.remove();
			if(!target.find('span').is('.tag')){
				$('<span class="hint">タグをドロップしてください</span>').appendTo(target)
			}
			flagDirty();
		});
	}

}
	
function buildResult(json){
	$('#result').children('span').remove();
	var d = $('#result');
	
	for (var n in json.tags) {
		var t = json.tags[n];
		createTag(d, t.key.id, t, false, t.access)
	}
}

function createTagMailBox(tags, option, email){
	var text = "";
	
	var tagsId = 'tags-'+formNum;
	var emailId = 'email-'+formNum;
	var optionId = 'option-'+formNum;
	var removeId = 'rem-'+formNum;
	formNum++;
	
	text +='<div class="boxed">';
	text +='<span id="'+tagsId+'" class="tagField">タグ : ';
	if(tags == null){
		text +='<span class="hint">タグをドロップしてください</span>';
	}
	text +='</span>';
	text +=' <span class="field_value">';
	text +='<select class="select" name="'+optionId+'" size="1">';
	text +='<option value="and">すべて</option>';
	if(option =="or"){
		text +='<option value="or" selected>どれか一つ</option>';	
	}else{
		text +='<option value="or">どれか一つ</option>';
	}
	text　+='</select>';
	text +='</span>'; 
	text　+=' を含むメッセージを'
	text　+='<br/>';
	text +='<span class="field_value">';
	if(email != null){
		text +='<input class="email" type="text" id="'+emailId+'" value="'+email+'"/>';
	}else{
		text +='<input class="email" type="text" id="'+emailId+'" value="${email}"/>';
	}
	text +='</span> に転送します'; 
	text +='<input class="removeTagMailButton" type="button" id="'+removeId+'" value="削除"/>';
	text +='</div>';

	var sp = $(text);
	sp.appendTo($('#tagMail'));
	
	$('#'+tagsId).ready(function(){
		toDroppable($('#'+tagsId));
		if(tags != null) {
			tags.children("span").appendTo($('#'+tagsId));
		}
	});
	
	$('#'+removeId).click(function(){
		sp.remove();
		flagDirty();
	});
}

$(document).ready(function() {
	$(window).bind('beforeunload', function(e) {
		if(dirty){
	    	return "変更が保存されていません！";
		}
	});
	
	$('#searchButton').click(function() {
		$('#searching').html("検索中...");
		var f = $("#searchForm");
		$.ajax({
			type : 'post',
			url : 'tagSearchJson',
			data : f.serialize(),
			success : function(json) {
				buildResult(json);
				$('#searching').html("");
			}
		});
	});
	
	$('#createButton').click(function() {
		var f = $("#searchForm");
		$.ajax({
			type : 'post',
			url : 'tagCreateJson',
			data : f.serialize(),
			success : function(json) {
				buildResult(json);
			}
		});
	});

	$('#postButton').click(function() {
		post();
	});
	
	$('#addTagMailButton').click(function(){
		createTagMailBox(null, "", null);
		flagDirty();
	});
});

function post(){

	var d ="";
	
	$("#tagMail").children('div').each(function(){
		var tag = $(this);
		var tagField = tag.find(".tagField");
		var select = tag.find(".select").find("option:selected");
		var email = tag.find(".email");
		
		var tags = "";
		tagField.find('.tag').each(function(){
			tags += $(this).attr('id').replace("tag-","")+","; 
		});

		d += 'tagMailArray='+email.attr('value')+','+select.attr('value')+','+tags+'&';
	});
	
	$.ajax({
		type : 'post',
		url : 'tagMailSetJson',
		data : d,
		success : function(json) {
			unFlagDirty();
		}
	});
	
}
</script>

<title><fmt:message key='msg.tagmailer' /></title>
</head>
<body>
	<jsp:include page="/shared/header.jsp">
		<jsp:param value="<fmt:message key='msg.tagmailer' />" name="name" />
	</jsp:include>
	<%@ include file="/niwaruka/member/nav.jsp"%>
	
	<section id="tagAreas">
		<article>

			<form>
				<div class="boxed top">
					<input id="postButton" type="button" value="保存" /> <span
						class="postLabel" id="postLabel"></span> <input
						id="addTagMailButton" type="button" value="転送設定を追加" />
					<div id="tagMail"></div>
				</div>

				<div id="ownedTagArea" class="boxed left">
					<div class="postButton">あなたが付けているタグ</div>
					<c:forEach var="e" items="${tags}">
						<c:choose>
							<c:when test='${fn:startsWith(f:h(e.tag),"#")}'>
								<span id="tag-${f:h(e.key.id)}-span"> <span
									id="tag-${f:h(e.key.id)}" class="tag twitter">${f:h(e.tag)}</span>
								</span>
							</c:when>
							<c:when test='${fn:startsWith(f:h(e.access),"sharedTag")}'>
								<span id="tag-${f:h(e.key.id)}-span"> <span
									id="tag-${f:h(e.key.id)}" class="tag shared">${f:h(e.tag)}</span>
								</span>
							</c:when>
							<c:when test='${fn:startsWith(f:h(e.access),"secretTag")}'>
								<span id="tag-${f:h(e.key.id)}-span"> <span
									id="tag-${f:h(e.key.id)}" class="tag secret">${f:h(e.tag)}</span>
								</span>
							</c:when>
							<c:otherwise>
								<span id="tag-${f:h(e.key.id)}-span"> <span
									id="tag-${f:h(e.key.id)}" class="tag public">${f:h(e.tag)}</span>
								</span>
							</c:otherwise>
						</c:choose>
					</c:forEach>
				</div>
			</form>

			<div id="tagSearchArea" class="boxed right">
				<span class="field_name">タグ検索</span> <span class="postLabel"
					id="searching"></span>
				<form id="searchForm" onsubmit="return false" action="#">
					<div>
						<span class="field_value"><input type="text" name="query" />
						</span> <input id="searchButton" type="submit" value="さがす" />
					</div>
					<div id="result"></div>
				</form>
			</div>


		</article>
	</section>

	<%@ include file="/shared/footer.jsp"%>
</body>
</html>
