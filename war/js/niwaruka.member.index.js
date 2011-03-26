/**
 * にわるかのJavascriptをまとめた物 要整理
 */

// 取得した最新の発言
var latestTweetTime;
// 更新チェックフラグ
var shouldCheck = true;
// 取得した最古の発言
var oldestTweetTime;

// 更新チェック回数、maxTrial回を超えると自動確認を停止
var tried = 0;
// 最大自動チェック回数
var maxTrial = 3;

/**
 * つぶやきを送信
 */
function tweet() {
	f = $("#tweetForm");
	
	var tags = "";
	$('.tag-toggled').each(function(){
		var sss = $(this).attr('for').replace('check-','tagArray=');
		tags+=""+sss+"&";
	});
	
	$.ajax({
		type : 'post',
		url : '/niwaruka/member/tweetJson',
		data : f.serialize()+"&"+tags,
		success : function(json) {
			clearForm($(f));
			refresh();
		}
	});
}

/**
 * 更新チェック
 */
function updateCheck() {

	$.ajax({
		type : 'post',
		url : '/niwaruka/member/updateCheckJson',
		data : 'time=' + latestTweetTime,
		success : function(json) {
			if (json.status === 'OK') {
				if (json.newTweets > 0) {
					var ntb = $('#new-tweets-bar');
					ntb.attr("class", "new-tweets-bar");

					ntb.children('span').remove();
					var text = "<span>";
					// text += "新しいつぶやきが" + json.newTweets + "件あります"
					text += "新しいつぶやきがあります！"
					text += "</span>"
					$(text).appendTo('#new-tweets-bar');
					shouldCheck = false;
				}else{
					tried++;
					if(tried > maxTrial){
						shouldCheck = false;
						var ntb = $('#new-tweets-bar');
						ntb.attr("class", "new-tweets-bar");

						ntb.children('span').remove();
						var text = "<span>";
						text += "新しいつぶやきを確認"
						text += "</span>"
						$(text).appendTo('#new-tweets-bar');
					}
				}
			}
		}
	});
}

/**
 * 何らかの要求チェック
 */
function checkRequests() {
	$(function() {
		$.ajax({
			type : 'post',
			url : '/niwaruka/member/tagRequestCheckJson',
			success : requestJson
		});
	});
}

/**
 * 要求チェックのコールバック
 * 
 * @param json
 */
function requestJson(json) {
	if (json.requests.length > 0) {
		$("#slideDown").fadeIn(2000);

		var sdcb = $("#slideDownContentBody");
		sdcb.children().remove();

		var str = "<div id='div'>"
		str += "<ul>";
		str += "</ul>";
		str += "</div>";
		var ul = $(str);
		ul.appendTo(sdcb);

		for (n in json.requests) {
			var str = "";
			var request = json.requests[n];
			var id = "" + request.key.id;
			str += "<li id='entry-"+id+"' class='list'>"
					+ "<form action='tagRequestDetermine'>"
					+ "<div class='tag-request'>"
					+ "<div>"

					+ request.requester.model.userName
					+ "さんから "
					+ toTagMini(request.tag.model, request.tag.model.tag,
							null) + " タグの使用がリクエストされています" + "</div>";
			if (request.comment != null) {
				str += "<div class='comment'>" + request.comment + "</div>";
			}
			str += "<div class='right'>"
					+ "<input id='yes-"+id+"' type='button' value='いいよ〜'/>"
					+ "<input id='no-"+id+"' type='button' value='だめ〜'/>"
					+ "</div>";
			str += "</div>" + "</form>" + "</li>";

			var list = $(str);
			list.appendTo(ul);

			var yesfunc = function(e) {
				$
						.ajax({
							type : 'post',
							url : 'tagRequestDetermineJson',
							data : 'id=' + e.target.id,
							success : function(json) {
								var s = "" + e.target.id;
								s = s.replace("yes-", "");
								$('#entry-' + s).remove();
								if ($('#div').find('li').length == 0) {
									$(
											"<span class='tag-request'>お知らせはありません</span>")
											.appendTo($('#div'));
								}
							}
						});
			};
			$("#yes-" + id).click(yesfunc);

			var nofunc = function(e) {
				$.ajax({
					type : 'post',
					url : 'tagRequestDetermineJson',
					data : 'id=' + e.target.id,
					success : function(json) {
						var s = "" + e.target.id;
						s = s.replace("no-", "");
						$('#entry-' + s).remove();

						if ($('#div').find('li').length == 0) {
							$("<span class='tag-request>お知らせはありません</span>")
									.appendTo($('#div'));
						}
					}
				});
			};
			$("#no-" + id).click(nofunc);
		}
	}
}


/**
 * 削除処理
 * 
 * @param id
 */
function remove(id){
	confirmDialog('本当に削除しますか？', '確認', 'OK', 'キャンセル',
		      function(cancel){
		        if (cancel) return;
		        $.ajax({
					type : 'post',
					url : '/niwaruka/member/deleteJson',
					data : "tweetId="+id,
					success : (function(){
						// box.slideUp("normal");
						refresh();
					})
				});
		      });
}

/**
 * 返信処理
 * 
 * @param id
 */
function reply(id){
	var box = $('#'+id+'-box');
	if(box.children("form").length == 0){
		var form = $("<form action='#' id='form-"+id+"'>" +
				"<input type='hidden' name='id' value='"+id+"'>" +
				"<textarea class='tweet-box' name='tweet' title='メッセージ？'></textarea></form>");
		//リプライボタン
		var button = $("<input type='button' value='りぷらい'/>");
		button.click(function(){
			$(function() {
				$.ajax({
					type : 'post',
					url : '/niwaruka/member/replyJson',
					data : form.serialize(),
					success : (function(){
						// box.slideUp("normal");
						refresh();
					})
				});
			});
		});
		button.appendTo(form);
		form.appendTo(box);
// form.ready(function(){
// form.inputHintOverlay(5, 7);
// });
	}
	
	if (box.is(":hidden")) {
		box.slideDown("normal");
	} else {
		box.slideUp("normal");
	}
}

/**
 * 現在取得している最古のつぶやき以前のつぶやきを取得
 */
function fetchOlderTweets() {
	var lm = $('#loading-message');
	lm.attr("class", "loading-message");	

	var ntb = $('#older-tweets-bar');
	ntb.attr("class", "new-tweets-bar hidden");
	
	$
	.ajax({
		type : 'get',
		url : '/niwaruka/member/tweetFetchJson',
		data : 'olderThan=' + oldestTweetTime,
		success : function(json) {
			if (json.status === 'OK') {
				var text ='<div>';
				
				for (var n in json.tweets) {
					var t = json.tweets[n];
					text+= makeTweetBlock(t);

					if (oldestTweetTime == null || oldestTweetTime > t.time) {
						oldestTweetTime = t.time;
					}
				}
				text += "</div>"
				$(text).appendTo('#message');
				shouldCheck = true;
				// var ntb = $('#new-tweets-bar');
				// ntb.attr("class", "new-tweets-bar hidden");
			} else {
				alert(json.errorMessage);
			}
			lm.attr("class", "loading-message hidden");
			ntb.attr("class", "new-tweets-bar");
		}
	});
	
}

function refresh() {

	var lm = $('#loading-message');
	lm.attr("class", "loading-message");
	

	var ntb = $('#new-tweets-bar');
	ntb.attr("class", "new-tweets-bar hidden");
	
	tried = 0;
	$
			.ajax({
				type : 'get',
				url : 'tweetFetchJson',
				success : function(json) {
					if (json.status === 'OK') {
						$('#message').children('div').remove();
						
						var text ='<div>';
						for (var n in json.tweets) {
							var t = json.tweets[n];
							text+= makeTweetBlock(t);
							
							if (latestTweetTime == null || latestTweetTime < t.time) {
								latestTweetTime = t.time;
							}
							if (oldestTweetTime == null || oldestTweetTime > t.time) {
								oldestTweetTime = t.time;
							}
						}
						text += "</div>";
						
						$(text).appendTo('#message');
						shouldCheck = true;
						var ntb = $('#older-tweets-bar');
						ntb.attr("class", "new-tweets-bar");
					} else {
						alert(json.errorMessage);
					}
					lm.attr("class", "loading-message hidden");
				}
			});
}

function clearForm(ele) {
	$(ele).find(':input').each(function() {
		switch (this.type) {
		case 'password':
		case 'select-multiple':
		case 'select-one':
		case 'text':
		case 'textarea':
			$(this).val('');
			break;
		case 'checkbox':
		case 'radio':
			this.checked = false;
		}
	});
}

