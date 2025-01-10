
$("a.writeBoardBtn").on("click", function(e) { /*게시글 등록*/
	e.preventDefault();
	let formName = $(this).data("formname");
	const $form = $(`form.${formName}`);
	if (!$form.find("input[name=boardTitle]").val().trim()) { alert("제목을 입력하세요"); return; }
	if (!$form.find("textarea[name=boardContent]").val().trim()) { alert("내용을 입력하세요"); return; }
	$form.submit();
});
