$("a.writeBoardBtn").on("click", function(e) {
	e.preventDefault();
	const className = $(this).data("formClass");
	const $form = $(`form.${className}`);
	if (!validateForm($form)) { return; }
	let html = createFileInfoInputs($thumbnailUl, "original");
	$form.append(html).submit();
});

$("a.updateBoardBtn").on("click", function(e) {
	e.preventDefault();
	const className = $(this).data("formClass");
	const $form = $(`form.${className}`);
	if (!validateForm($form)) { return; }
	let html = createFileInfoInputs($thumbnailUl, "new");
	html += createFileInfoInputs($thumbnailUl, "remove");
	$form.append(html).submit();
});

$("a.deleteBoardBtn").on("click", function(e) {
	e.preventDefault();
	if (!confirm("정말로 삭제하시겠습니까?")) { return; }
	location.href = $(this).attr("href");
});


$("a.page").on("click", function(e) { /*board-list page click*/
	e.preventDefault();
	const $pageForm = $("form[name=pageForm]");
	$pageForm.find("input[name=page]").val($(this).attr("href"));
	$pageForm.submit();
});

$("form[name=searchForm").on("submit", function(e) { /*게시판 검색*/
	e.preventDefault();
	if (!$(this).find("select[name=type]").val()) {
		alert("카테고리를 선택하세요");
		return;
	}
	if (!$(this).find("input[name=keyword]").val()) {
		alert("내용을 입력하세요");
		return;
	}
	$(this).find("input[name=page]").val(1);
	this.submit();
});


function validateForm($form) {
	if (!$form.find("input[name=boardTitle]").val().trim()) { alert("제목을 입력하세요."); return false; }
	if (!$form.find("textarea").val().trim()) { alert("내용을 입력하세요"); return false; }
	return true;
}











