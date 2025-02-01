let commentService = (function() {
	function showComments(callback) {
		$.ajax({
			url: `${contextPath}/comments/${boardNum}?page=${page}`,
			method: 'GET',
			dataType: 'json',
			success: function(comments) {
				if (callback) {
					callback(comments);
				}
			}
		});
	}

	function insert(comment, callback) {
		$.ajax({
			url: `${contextPath}/comments/${boardNum}`,
			method: 'post',
			contentType: 'application/json;charset=UTF-8',
			data: JSON.stringify(comment),
			success: function() {
				if (callback) {
					callback();
				}
			}
		});
	}

	function remove(cno, callback) {
		$.ajax({
			url: `${contextPath}/comments/${cno}`,
			method: 'delete',
			success: function() {
				if (callback) {
					callback();
				}
			}
		});
	}

	function update(comment, callback) {
		$.ajax({
			url: `${contextPath}/comments/${comment.cno}`,
			method: 'patch',
			contentType: 'application/json;charset=UTF-8',
			data: JSON.stringify(comment),
			success: function() {
				if (callback) {
					callback();
				}
			}
		});
	}
	return { showComments: showComments, insert: insert, remove: remove, update: update }
})();


$("a.writeCommentBtn").on("click", function(e) { /*댓글등록*/
	e.preventDefault();
	if (!sessionUserNum) { alert("로그인 후 사용하실 수 있습니다."); return; }
	let $textarea = $(this).parent().prev("textarea");
	let commentContent = $textarea.val().trim();
	console.log(commentContent);
	console.log($textarea);
	if (!commentContent) { alert("댓글을 입력하세요"); return; }
	commentService.insert({ commentContent: commentContent }, () => {
		$textarea.val("");
		commentService.showComments(createCommentHtml);
	});
});

$commentUl.on("click", ".removeCommentBtn", function(e) { /*댓글삭제*/
	e.preventDefault();
	if (!confirm("정말로 삭제하시겠습니까?")) { return; }
	let cno = $(this).closest("li").data("cno");
	commentService.remove(cno, () => { commentService.showComments(createCommentHtml); });
});

$commentUl.on("click", ".updateCommentBtn", function(e) { /*댓글수정준비*/
	e.preventDefault();
	if (flag) { return; }
	flag = true;
	const $li = $(this).closest("li");
	let commentContent = $li.find("span.commentContent").text();
	$updateLi.data("cno", $li.data("cno"));
	$updateLi.find("textarea").val(commentContent);
	$li.hide().after($updateLi);
	$updateLi.show();
});

$("a.cancelCommentBtn").on("click", function(e) { /*수정,답글 취소*/
	e.preventDefault();
	moveToUpdateDiv($updateLi.find("textarea"), $updateLi);
});

$("a.updateSubmitBtn").on("click", function(e) { /*댓글수정 submit*/
	e.preventDefault();
	const $textarea = $updateLi.find("textarea");
	let commentContent = $textarea.val().trim();
	if (!commentContent) { alert("댓글을 입력하세요"); return; }
	let comment = { cno: $updateLi.data("cno"), commentContent: commentContent };
	commentService.update(comment, () => {
		moveToUpdateDiv($textarea, $updateLi);
		commentService.showComments(createCommentHtml);
	});
});

$commentUl.on("click", ".replyBtn", function(e) { /*답글준비*/
	e.preventDefault();
	if (flag) { return; }
	flag = true;
	const $li = $(this).closest("li");
	const $replySubmitBtn = $updateLi.find(".replySubmitBtn");
	let pcno = $li.data("pcno");
	let pid = $li.data("userid");
	$updateLi.data("pcno", pcno);
	$updateLi.data("pid", pid);
	$replySubmitBtn.prev().hide();
	$replySubmitBtn.css("display", "inline-block").show();
	$li.after($updateLi);
	$updateLi.show();
});


$(".replySubmitBtn").on("click", function(e) { /*답글submit*/
	e.preventDefault();
	const $textarea = $updateLi.find("textarea");
	let commentContent = $textarea.val().trim();
	if (!commentContent) { alert("댓글을 입력하세요"); return; }
	let comment = { pcno: $updateLi.data("pcno"), pid: $updateLi.data("pid"), commentContent: commentContent };
	commentService.insert(comment, () => {
		$(this).hide().prev().show();
		moveToUpdateDiv($textarea, $updateLi);
		commentService.showComments(createCommentHtml);
	});
});


function moveToUpdateDiv($textarea, $updateLi) { /*댓글수정submit,취소 후처리 */
	$textarea.val("");
	$updateLi.prev().show();
	$updateLi.hide().appendTo($("div.updateDiv"));
	flag = false;
}


function createCommentHtml(comments) {
	let html = "";
	comments.forEach(comment => {
		html += `<li data-cno="${comment.cno}" data-pcno="${comment.pcno}" data-userid="${comment.userId}">`;
		html += `<span class="profile">`;
		html += `<i class="fa fa-user-circle" aria-hidden="true"></i>`;
		html += `</span>`;
		html += `<section>`;
		html += `<div>`;
		html += `<span class="writer">${comment.userId}</span>`;
		html += `</div>`;
		html += `<div>`;
		html += `<span class="replyComment">${comment.pid != null ? '@' + comment.pid +' ': ''}</span>`;
		html += `<span class="commentContent">${comment.commentContent}</span>`;
		html += `</div>`;
		html += `<div class="comment-footer">`;
		html += `<span>${comment.commentRegisterDate}</span>`;
		if (comment.userNum == sessionUserNum) {
			html += ` <a href="#" class="updateCommentBtn">수정</a>`;
			html += `<a href="#" class="removeCommentBtn">삭제</a>`;
		} else if (sessionUserNum) {
			html += `<a href="#" class="replyBtn">답글</a>`;
		}
		html += `</div>`;
		html += `</section>`;
		html += `</li>`;
	});
	$commentUl.empty().append(html);
}