fileService.getFiles(); /*썸네일을 불러온다*/

let fileArr = new Array();
$(".thumbnailUl > li").each((i, li) => {
	fileArr[i] = { uuid: li.dataset.uuid, uploadPath: li.dataset.uploadpath, fileName: li.dataset.filename, fileType: li.dataset.filetype };
});
$(".thumbnailUl").empty();
updateBoard(fileArr);



function updateBoard(fileArr) { /*게시글 수정*/
	$("a.updateBoardBtn").on("click", function(e) { 
		e.preventDefault();
		let formName = $(this).data("formname");
		const $form = $(`form.${formName}`);
		if (!$form.find("input[name=boardTitle]").val().trim()) { alert("제목을 입력하세요"); return; }
		if (!$form.find("textarea[name=boardContent]").val().trim()) { alert("내용을 입력하세요"); return; }
		fileArr.forEach(file => {
			fileService.remove(file);
		});
		createFileSubmitInput($form);
	});
}