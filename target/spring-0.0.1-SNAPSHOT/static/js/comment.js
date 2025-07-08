window.boardNum = $("#board-container").data("boardNum");
let page = 1;
const $commentUl = $("ul.comment-ul");
const $updateLi = $("li.update-li");
const status = { isEditing: false };
const commentService = (function() {
	function insert(comment, callback) {
		$.ajax({
			url: `${contextPath}/comments/${comment.boardNum}`,
			method: 'post',
			contentType: 'application/json;charset=UTF-8',
			data: JSON.stringify(comment),
			success: callback
		});
	}
	function update(comment, callback) {
		$.ajax({
			url: `${contextPath}/comments/${comment.commentNum}`,
			method: 'patch',
			contentType: 'application/json;charset=UTF-8',
			data: JSON.stringify(comment),
			success: callback
		});
	}
	function remove(commentNum, callback) {
		$.ajax({
			url: `${contextPath}/comments/${commentNum}`,
			method: 'delete',
			success: callback
		});
	}
	function getComments(boardNum, page, callback) {
		$.ajax({
			url: `${contextPath}/comments/${boardNum}?page=${page}`,
			method: 'get',
			success: callback
		});
	}
	return { insert, update, remove, getComments };
})();

(function() {
	if ($("#board-container").data("boardDetail")) {
		showComments($commentUl, boardNum, page);
	}
})();


$("a.comment-insert-btn").on("click", function(e) {
	e.preventDefault();
	const $textarea = $(this).closest(".comment-input").find("textarea");
	if (!sessionUserNum) { alert("로그인 후 사용하실 수 있습니다."); $textarea.val(""); return; }
	const commentContent = $textarea.val().trim();
	if (!commentContent) { alert("댓글을 입력하세요."); return; }
	const comment = { boardNum, commentContent };
	commentService.insert(comment, () => {
		$textarea.val("");
		showComments($commentUl, boardNum, page);
	});
});

$commentUl.on("click", ".comment-remove-btn", function(e) {
	e.preventDefault();
	if (!confirm("정말로 삭제하시겠습니까?")) { return; }
	const commentNum = $(this).closest("li").data("commentNum");
	commentService.remove(commentNum, () => showComments($commentUl, boardNum, page));
});


$("a.comment-cancel-btn").on("click", function(e) {
	e.preventDefault();
	resetUpdateLi($updateLi);
	setStatus(false);
});

$commentUl.on("click", ".comment-update-btn", function(e) {
	e.preventDefault();
	if (getStatus()) { return; }
	setStatus(true);
	const $li = $(this).closest("li");
	const $textarea = $updateLi.find("textarea");
	const commentNum = $li.data("commentNum");
	const commentContent = $li.find(".comment-content").text();
	$textarea.val(commentContent);
	$updateLi.data("commentNum", commentNum).show();
	$li.hide().after($updateLi);
});

$commentUl.on("click", ".comment-reply-btn", function(e) {
	e.preventDefault();
	if (getStatus()) { return; }
	setStatus(true);
	const $li = $(this).closest("li");
	$updateLi.data("parentId", $li.data("userId")).data("parentNum", $li.data("parentNum")).show();
	$updateLi.find("a.comment-reply-done").css("display", "inline-block").prev().hide();
	$li.after($updateLi);
});

$("a.comment-update-done").on("click", function(e) {
	e.preventDefault();
	const $textarea = $updateLi.find("textarea");
	const commentContent = $textarea.val().trim();
	if (!commentContent) { alert("댓글을 입력하세요"); return; }
	const commentNum = $updateLi.data("commentNum");
	const comment = { commentNum, commentContent };
	commentService.update(comment, () => {
		resetUpdateLi($updateLi);
		setStatus(false);
		showComments($commentUl, boardNum, page);
	});
});

$("a.comment-reply-done").on("click", function(e) {
	e.preventDefault();
	const $textarea = $updateLi.find("textarea");
	const commentContent = $textarea.val().trim();
	if (!commentContent) { alert("댓글을 입력하세요"); return; }
	const comment = { commentContent, commentParentNum: $updateLi.data("parentNum"), commentParentId: $updateLi.data("parentId"), boardNum };
	commentService.insert(comment, () => {
		resetUpdateLi($updateLi);
		setStatus(false);
		showComments($commentUl, boardNum, page);
	});

});

$commentUl.on("click", ".comment-more-btn", function(e) {
	e.preventDefault();
	showComments($commentUl, boardNum, ++page);
});

function resetUpdateLi($updateLi) {
	$updateLi.find("textarea").val("");
	$updateLi.prev().show();
	$updateLi.find("a.comment-update-done").css("display", "inline-block").next().hide();
	$updateLi.hide().appendTo($("body"));
}

function showComments($ul, boardNum, page) {
	commentService.getComments(boardNum, page, commentsDto => appendComments($ul, commentsDto));
}

function getStatus() {
	return status.isEditing;
}

function setStatus(isEditing) {
	status.isEditing = isEditing;
}

function appendComments($ul, commentsDto) {
	let html = createComments(commentsDto);
	$ul.empty().append(html);
}

function createComments(commentsDto) {
	const comments = commentsDto.comments;
	const nextCountPage = commentsDto.nextCountPage;
	let html = "";
	comments.forEach(comment => {
		html += `${comment.commentNum != comment.commentParentNum ? '⤷' : ''}`;
		html += `<li data-comment-num="${comment.commentNum}" data-parent-num="${comment.commentParentNum}" data-user-id="${comment.userId}" data-user-num="${comment.userNum}">`;
		html += `<span class="profile">`;
		html += `<i class="fa fa-user-circle" aria-hidden="true"></i>`;
		html += `</span>`;
		html += `<section>`;
		html += `<div>`;
		html += `<span class="writer">${comment.userId}</span>`;
		html += `</div>`;
		html += `<div>`;
		html += `<span class="comment-parent-id">${comment.commentParentId == null ? '' : '@' + comment.commentParentId + ' '}</span>`;
		html += `<span class="comment-content">${comment.commentContent}</span>`;
		html += `</div>`;
		html += `<div class="comment-footer">`;
		html += `<span>${elapsetTime(comment.commentRegisterDate)}</span>`;
		html += createButtons(sessionUserNum, isAdmin, comment);
		html += `</div>`;
		html += `</section>`;
		html += `</li>`;
	});
	if (nextCountPage > 0) {
		html += `<a href="#" class="comment-more-btn" >댓글 더보기</a>`;
	}
	return html;
}

function createButtons(sessionUserNum, isAdmin, comment) {
	const isMycomment = sessionUserNum == comment.userNum;
	const canReply = sessionUserNum && !isMycomment && (comment.commentParentNum == comment.commentNum);
	let html = "";
	if (isAdmin || isMycomment) {
		html += `<a href="#" class="comment-update-btn">수정</a>`;
		html += `<a href="#" class="comment-remove-btn">삭제</a>`;
	}
	if (canReply) {
		html += `<a href="#" class="comment-reply-btn">답글</a>`;
	}
	return html;
}

function elapsetTime(date) {
	const currentDate = new Date();
	const registerDate = new Date(date);
	const gap = (currentDate - registerDate) / 1000;
	const times = [
		{ name: "년", milliseconds: 60 * 60 * 24 * 365 },
		{ name: "개월", milliseconds: 60 * 60 * 24 * 30 },
		{ name: "일", milliseconds: 60 * 60 * 24 },
		{ name: "시간", milliseconds: 60 * 60 },
		{ name: "분", milliseconds: 60 },
	];
	for (const time of times) {
		let result = Math.floor(gap / time.milliseconds);
		if (result > 0) {
			return `${result}${time.name}전`;
		}
	}
	return "방금전";
}
