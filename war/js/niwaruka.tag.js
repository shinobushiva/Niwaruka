//更新チェックフラグ
var shouldCheck = true;
// 取得した最古の発言
var oldestTweetTime;

function getTweets(id) {

	var dt = new Date();
	if (!oldestTweetTime)
		oldestTweetTime = dt.getTime();

	tried = 0;
	$.ajax({
		type : 'get',
		url : '/niwaruka/tweetFetchJson',
		data : 'tagId=' + id + '&olderThan=' + oldestTweetTime,
		success : function(json) {
			if (json.status === 'OK') {
				$('#message').children('div').remove();

				var text = '<div>';
				for ( var n in json.tweets) {
					var t = json.tweets[n];

					text += makeTweetBlock(t, true);

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
			// lm.attr("class", "loading-message hidden");
		}
	});

}

function getOlderTweets(id) {
	// var lm = $('#loading-message');
	// lm.attr("class", "loading-message");

	var ntb = $('#older-tweets-bar');
	ntb.attr("class", "new-tweets-bar hidden");

	$.ajax({
		type : 'get',
		url : '/niwaruka/tweetFetchJson',
		data : 'tagId=' + id + '&olderThan=' + oldestTweetTime,
		success : function(json) {
			if (json.status === 'OK') {
				var text = '<div>';

				for ( var n in json.tweets) {
					var t = json.tweets[n];
					text += makeTweetBlock(t, true);

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
			// lm.attr("class", "loading-message hidden");
			ntb.attr("class", "new-tweets-bar");
		}
	});

}
