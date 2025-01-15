let commentService = (function() {
	function showComments() {
		$.ajax({
			url: `${contextPath}/comments/${boardNum}?page=${page}`,
			method: "get",
			dataType: "JSON",
			success: function(comments) {
				createComments(comments);
			}
		});
	}

	function insert(comment, callback) {
		$.ajax({
			url: `${contextPath}/comments/${boardNum}`,
			method: "post",
			contentType: "application/json;charset=UTF-8",
			data: JSON.stringify(comment),
			success: function() {
				if (callback) {
					callback();
				}
			}
		});
	}

	function remove(cno) {
		$.ajax({
			url: `${contextPath}/comments/${cno}`,
			method: 'delete',
			success: function() {
				commentService.showComments();
			}
		});
	}

	function update(comment, callback) {
		$.ajax({
			url: `${contextPath}/comments/${comment.cno}`,
			method: "patch",
			contentType: "application/json;charset=UTF-8",
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



$(".writeCommentBtn").on("click", function(e) { /*댓글등록*/
	e.preventDefault();
	if (!sessionUserNum) { alert("로그인 후 사용하실 수 있습니다."); return; }
	const $textarea = $(this).closest("div.commentInput").find("textarea");
	let commentContent = $textarea.val().trim();
	if (!commentContent) { alert("댓글을 입력하세요"); return; }
	let comment = { commentContent: commentContent };
	commentService.insert(comment, () => {
		$textarea.val("");
		commentService.showComments();
	});
});


$(".commentUl").on("click", ".deleteCommentBtn", function(e) { /*댓글삭제*/
	e.preventDefault();
	if (!confirm("정말로 삭제 하시겠습니까 ?")) { return; }
	let cno = $(this).closest("li").data("cno");
	commentService.remove(cno);
});


$(".commentUl").on("click", ".updateCommentBtn", function(e) { /*댓글수정준비*/
	e.preventDefault();
	if (flag) { return; }
	flag = true;
	const $li = $(this).closest("li");
	let cno = $li.data("cno");
	let commentContent = $li.find("span.commentContent").text();
	$updateLi.data("cno", cno);
	const $textarea = $updateLi.find("textarea");
	$textarea.val(commentContent);
	$li.hide();
	$li.after($updateLi);
	$updateLi.show();
});


$("a.cancelCommentBtn").on("click", function(e) { /*수정,답글 취소*/
	e.preventDefault();
	$updateLi.find("textarea").val("");
	$updateLi.hide().prev().show();
	$updateLi.appendTo(".updateDiv");
	flag = false;
});

$(".updateSubmitBtn").on("click", function(e) { /*댓글수정 전송*/
	e.preventDefault();
	const $textarea = $updateLi.find("textarea");
	let cno = $updateLi.data("cno");
	let commentContent = $textarea.val().trim();
	if (!commentContent) { alert("댓글을 입력해주세요"); return; }
	let comment = { cno: cno, commentContent: commentContent };
	commentService.update(comment, () => {
		$textarea.val("");
		$updateLi.prev().show();
		$updateLi.hide().appendTo($("div.updateDiv"));
		commentService.showComments();
		flag = false;
	});
});


$(".commentUl").on("click", ".replyBtn", function(e) { /*답글준비*/
	e.preventDefault();
	if (flag) { return; }
	flag = true;
	const $li = $(this).closest("li");
	let pcno = $li.data("pcno");
	let pid = $li.data("pid");
	$updateLi.data("pcno", pcno);
	$updateLi.data("pid", pid);
	const $replySubmitBtn = $updateLi.find("a.replySubmitBtn");
	$replySubmitBtn.show();
	$replySubmitBtn.prev().hide();
	$li.after($updateLi);
	$updateLi.show();
});


$(".replySubmitBtn").on("click", function(e) { /*답글등록*/
	e.preventDefault();
	const $textarea = $updateLi.find("textarea");
	const $replySubmitBtn = $(this);
	let pcno = $updateLi.data("pcno");
	let pid = $updateLi.data("pid");
	let commentContent = $textarea.val().trim();
	if (!commentContent) { alert("댓글을 입력하세요"); return; }
	let comment = { pcno: pcno, pid: pid, commentContent: commentContent };
	commentService.insert(comment, () => {
		$textarea.val("");
		$replySubmitBtn.hide();
		$replySubmitBtn.prev().show();
		$updateLi.hide().appendTo($(".updateDiv"));
		commentService.showComments();
		flag = false;
	});
});



function createComments(comments) {
	let html = "";
	comments.forEach(comment => {
		html += `<li data-cno="${comment.cno}" data-pid="${comment.userId}" data-pcno="${comment.pcno}">`;
		html += `<span class="profile">`;
		html += `<i class="fa fa-user-circle" aria-hidden="true"></i>`;
		html += `</span>`;
		html += `<section>`;
		html += `<div>`;
		html += `<span class="writer">${comment.userId}</span>`;
		html += `</div >`;
		html += `<div>`;
		html += `<span class="pid">${comment.pid != null ? '@' + comment.pid + ' ' : ''}</span>`;
		html += `<span class="commentContent">${comment.commentContent}</span>`;
		html += `</div >`;
		html += `<div>`;
		html += `<span>${comment.commentRegisterDate}</span>`;
		if (comment.userNum == sessionUserNum) {
			html += `<a href = "#" class="updateCommentBtn"> 수정</a >`;
			html += `<a href = "#" class="deleteCommentBtn"> 삭제</a >`;
		} else if (sessionUserNum) {
			html += `<a href = "#" class="replyBtn"> 답글</a>`;
		}
		html += `</div > `;
		html += `</section > `;
		html += `</li > `;
	})
	$(".commentUl").empty().append(html);
}