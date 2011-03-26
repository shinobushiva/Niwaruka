<%@page pageEncoding="UTF-8" isELIgnored="false" session="false"%>
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
<link rel="stylesheet" href="/css/tagManager.css" />
<script src="/js/jquery-1.4.3.js" type="text/javascript"></script>
<script type="text/javascript" src="/js/jquery-ui-1.8.6.custom.min.js"></script>
<script type="text/javascript" src="/js/jquery.qtip-1.0.0-rc3.min.js"></script>

<c:forEach var="e" items="${tags}">
	<script type="text/javascript">
	$(document).ready(function() {
		t = $("#check-${e.key.id}-span");
		
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
	
		t.click(function() {
			label = $("#check-${e.key.id}-label");
			label.toggleClass("tag-toggled");
			label.toggleClass("tag-untoggled");
			flagDirty();
			return false;
		});
		
	});
</script>
</c:forEach>

<script type="text/javascript">

$(document).bind('touchmove', function(e) {
	   e.preventDefault();
	}, false);
	
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
		if(ui.draggable.parent().attr('id') == 'result'){
			var label = ui.draggable.children('label');
			var str = label.attr('class');
			str = str.replace('tag', 'tag-toggled');
			label.attr('class', str);
			ui.draggable.children('input').bind("click", function() {
				label.toggleClass("tag-toggled");
				label.toggleClass("tag-untoggled");
				flagDirty();
			});
		}
		ui.draggable.appendTo(target);
		flagDirty();
	}


	$(document).ready(function() {
		$('#usedTagArea').droppable({
			drop : function(e, ui) {
				tag(e,ui, this);
			}
		});
		$('#unUsedTagArea').droppable({
			drop : function(e, ui) {
				tag(e, ui, this);
			}
		});
	});
	
function buildResult(json){
	$('#result').children('span').remove();
	var d = $('#result');
	
	for (var n in json.tags) {
		var t = json.tags[n];
		
		if($('#check-'+t.key.id+'-span').attr('id')){
			continue;
		}
		
		var text = "";
		text += '<span id="check-'+t.key.id+'-span" class="inline">';
		text += '<input class="hidden-checkbox" id="check-'+t.key.id+'" type="checkbox" checked="checked" name="tagArray" value="'+t.key.id+'"/>'
		if(t.tag.indexOf("#") == 0){
			text += '<label for="check-'+t.key.id+'" id="check-'+t.key.id+'-label" class="tag twitter">'+t.tag+'</label>';
		} else if (t.access.indexOf("sharedTag") != -1) {
			text += '<label for="check-'+t.key.id+'" id="check-'+t.key.id+'-label" class="tag shared">'+t.tag+'</label>';
		} else if (t.access.indexOf("secretTag") != -1) {
			text += '<label for="check-'+t.key.id+'" id="check-'+t.key.id+'-label" class="tag secret">'+t.tag+'</label>';
		}else{
			text += '<label for="check-'+t.key.id+'" id="check-'+t.key.id+'-label" class="tag public">'+t.tag+'</label>';
		}
		text += '</span>';
		
		var sp = $(text);
		sp.appendTo(d);
		
		sp.ready(function() {
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
			
			tipping(sp, t);
		});
	}
}


function tippingWithText(sp, txt, id){
	//txt = txt+'<div class="tagDblInfo">ダブルクリックでタグのページが開きます<div>';
	txt = txt+'<div class="tagDblInfo"><a href="../tag/'+id+'">タグのページを開く</a><div>';
	sp.qtip({
		   position: { 
			   target: 'mouse',
			   screen:true 
			},
		   content : txt,
		   style: { 
			      width: 200,
			      padding: 2,
			      background: '#FFFFFF',
			      color: 'black',
			      textAlign: 'center',
			      border: {
			         width: 1,
			         radius: 3,
			         color: '#c75078'
			      },
			      tip: 'topLeft',
			      name: 'dark' // Inherit the rest of the attributes from the preset dark style
			   },
		   hide: { when: 'mouseout', fixed: true, delay:100 }
	 });
}

function tipping(sp, t){
	txt = "";
	if(t.tag.indexOf("#") == 0){
		txt = '<span class="tagInfo">'+'ハッシュタグ'+'</span>';
	}else if(!t.creatorRef.model){
   	    txt = '<span class="tagInfo">'+'公開タグ'+'</span>';
	}else if(t.access.indexOf("sharedTag")!=-1){
   	    txt = '<span class="tagInfo">'+'共有タグ : '+t.creatorRef.model.name+'</span>';
	}else if(t.access.indexOf("secretTag")!=-1){
   	    txt = '<span class="tagInfo">'+'秘密タグ : '+t.creatorRef.model.name+'</span>';
	}
	tippingWithText(sp, txt, t.key.id);
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
});

function post(){
	var tagspans = $("#usedTagArea").find('label').get();

	var d ="";
	
	for(ts in tagspans){
		var tag = tagspans[ts];
		//postButtonも拾われるのでidでチェック
		if(tag.id.indexOf("check-") != -1){
			if($(tag).hasClass("tag-untoggled")){
				d += "dTagArray="+tag.id.replace("check-","").replace("-label","")+"&";
			}else{
				d += "tagArray="+tag.id.replace("check-","").replace("-label","")+"&";
			}
		}
	}

	$.ajax({
		type : 'post',
		url : 'tagManageJson',
		data : d,
		success : function(json) {
			unFlagDirty();
		}
	});
	
}
</script>

<title>たぐまねーじゃ</title>
</head>
<body>
	<jsp:include page="/shared/header.jsp">
		<jsp:param value="たぐまねーじゃ" name="name" />
	</jsp:include>

	<jsp:include page="/niwaruka/member/nav.jsp" />

	<section id="tagAreas">
		<article>

			<form>

				<div id="unUsedTagArea" class="boxed top">
					<div>未使用タグエリア</div>
				</div>

				<div id="usedTagArea" class="boxed left">
					<div class="postButton">
						使用するタグをこのエリアにおいてください <input id="postButton" type="button"
							value="保存" /> <span class="postLabel" id="postLabel"></span>
					</div>
					<c:forEach var="e" items="${enabledTags}">
						<span id="check-${f:h(e.key.id)}-span" class="inline"> <input
							class="hidden-checkbox" id="check-${f:h(e.key.id)}"
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
							</c:choose> </span>
					</c:forEach>
					<c:forEach var="e" items="${disabledTags}">
						<span id="check-${f:h(e.key.id)}-span" class="inline"> <input
							class="hidden-checkbox" id="check-${f:h(e.key.id)}"
							type="checkbox" ${f:multibox("tagArray", f:h(e.key.id))}/> <label
							for="check-${f:h(e.key.id)}" id="check-${f:h(e.key.id)}-label"
							class="tag-untoggled">${f:h(e.tag)}</label> </span>
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
					<div>
						<input type="radio" name="type" value="publicTag">公開 <input
							type="radio" name="type" value="sharedTag">グループ <input
							type="radio" name="type" value="secretTag" checked>秘密 <input
							id="createButton" type="submit" value="つくる" />
					</div>
				</form>
				<div id="result"></div>
			</div>


		</article>
	</section>

	<jsp:include page="/shared/footer.jsp" />
</body>
</html>
