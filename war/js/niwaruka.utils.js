/**
 * にわるかのJavascriptをまとめた物 要整理
 */

/**
 * HTMLをエスケープ
 * 
 * @param str
 * @returns
 */
function escapeHTML(str) {
	if(str == null){
		return "";
	}
	return str.replace(/&/g, '&amp;').replace(/</g, '&lt;').replace(/>/g,
			'&gt;');
}

/**
 * URLにリンクを設定
 * 
 * @param str
 * @returns
 */
function linkUrl(str) {
	if(str == null){
		return "";
	}
	return str.replace(/(http:\/\/[\x21-\x7e]+)/gi,
			"<a target='_blank' href='$1'>$1</a>").
			replace(/(https:\/\/[\x21-\x7e]+)/gi,
			"<a target='_blank' href='$1'>$1</a>");
}

/**
 * ハッシュタグにリンクを設定
 * 
 * @param str
 * @returns
 */
function linkHashTag(str) {
	return str
			.replace(/#([\x21-\x7e]+)/gi,
					"<a target='_blank' href='http://twitter.com/search/%23$1'>#$1</a>");
}

/**
 * 時間をテキストに変換
 * 
 * @param d
 * @returns {String}
 */
function toTimeText(d){
	sec = Math.ceil((new Date().getTime()-d.getTime())/1000);
	if(sec < 60 ){
		return ""+sec+" seconds ago";
	}else if(sec < 60*60){
		return ""+ Math.ceil(sec/60) +" minutes ago";
	}else if(sec < 60*60*24){
		return ""+ Math.ceil(sec/(60*60)) +" hours ago";
	}else if(sec < 60*60*24*100){
		return ""+ Math.ceil(sec/(60*60*24)) +" days ago";
	}else{
		return "more than 100 days ago";
	}
}

/**
 * 小さなタグを作成
 * 
 * @param tag
 * @param inner
 * @param user
 * @returns {String}
 */
function toTagMini(tag, inner, user){
	var text ="";
	if (tag.tag.indexOf("#") == 0) {
	text += '<span class="tag-mini twitter">'
		+ inner;
	}else if(!tag.access){
		text += '<span class="tag-mini public">'
			+ inner;
	} 
	else if (tag.access.indexOf("sharedTag") != -1) {
		text += '<span class="tag-mini shared">'
			+ inner;
	} else if (tag.access.indexOf("secretTag") != -1) {
		text += '<span class="tag-mini secret">'
		+ inner;
	} else {
		text += '<span class="tag-mini public">'
		+ inner;
	}
	text += "</span>";
	return text;
}

/**
 * 発言ブロックを作成
 * 
 * @param t
 * @returns {String}
 */
function makeTweetBlock(t,  noModify){
	var text = "";
	text+= '<div class="tweet-block">';
	
	text += '<div class="tweet-image">';
	text += '<img height="48" width=48 src="/niwaruka/userImage?name='
			+ t.userDataRef.model.name + '"/>';
	text += '</div>';
	text += '<div class="tweet-content">';
	text += '<div class="tweet-row tweet-row-name">';
	text += '<span class="tweet-name">'
			+ escapeHTML(t.userDataRef.model.name)
			+ "</span>";
	text += '<span class="tweet-user-name">'
			+ escapeHTML(t.userDataRef.model.userName)
			+ "</span>";
	for (var n2 in t.tagList) {
		var inner = "<a href='/niwaruka/tag/"+t.tagList[n2].key.id+"'>"+escapeHTML(t.tagList[n2].tag)+"</a>";
		text += toTagMini(t.tagList[n2], inner, t.userDataRef.model);
	}
	text += "</div>";
	text += '<div class="tweet-row"><span class="tweet-text">'
			+ linkHashTag(linkUrl(escapeHTML(t.content)))
			+ "</span></div>";
	if (t.client != null) {
		var client = t.client;
		if (t.twitterUser != null)
			client = t.twitterUser + "が" + t.client;
		text += '<div class="tweet-row"><span class="tweet-timestamp">'
				+ toTimeText(new Date(t.time))	+" : "+ client
				+ "で発言 "
				+ "</span>";
	} else {
		text += '<div class="tweet-row">'
				+'<span class="tweet-timestamp">'
				+ toTimeText(new Date(t.time))
				+ "</span>";
	}
	if(!noModify){
		text +='<span class="icons">'
			+ '<a href="javascript:reply('+ t.key.id+');"><img src="/resources/reply.png"/></a>'
			+ "</span>";
	}
	if(!noModify && gUserId && gUserId == t.userDataRef.key.id ){
		text +='<span class="icons">'
			+ '<a href="javascript:remove('+ t.key.id+');"><img src="/resources/delete.png"/></a>'
			+ "</span>";
	}
	text +="</div>";
	text += '<div id="'+t.key.id+'-box" class="tweet-reply"></div>';
	
	text += '</div>';
	text += '</div>';
	
	return text;
}

function tippingWithText(sp, txt, id){
	// txt = txt+'<div class="tagDblInfo">ダブルクリックでタグのページが開きます<div>';
	txt = txt+'<div class="tagDblInfo"><a href="/niwaruka/tag/'+id+'">タグのページを開く</a><div>';
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
			      name: 'dark' // Inherit the rest of the attributes from the
								// preset dark style
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

/*
 * 確認ダイアログ
 * 
 * message : ダイアログのメッセージ本文 title : ダイアログのタイトル buttonok : OKボタンのテキスト buttoncancel :
 * キャンセルボタンのテキスト response : コールバック関数を指定する。引数 cancel にボタン選択の結果が入る。 OK ならば false
 * キャンセルならば true となる。
 */
function confirmDialog(message, title, buttonok, buttoncancel, response){
  var _dlg = $('<div>'+message+'</div>');
  var _buttons = {};
  _buttons[buttonok] = function(){$(this).dialog('close');response(false)};
  _buttons[buttoncancel]  = function(){$(this).dialog('close');response(true)};
  
  _dlg.dialog({
    modal:true,
    draggable:false,
    title:title,
    buttons:_buttons,
    overlay:{ opacity:0.3, background:'#225B7F' }
  });
}

