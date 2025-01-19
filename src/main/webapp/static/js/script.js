let submenuItems = document.querySelectorAll('ul.submenu');
let heightArray = new Array(submenuItems.length);

submenuItems.forEach((submenu, i) => {
	heightArray[i] = submenu.offsetHeight;
	submenu.style.height = '0px';
	submenu.style.visibility = 'visible';
});

document.querySelectorAll('nav>ul>li').forEach((li, i) => {
	li.addEventListener('mouseover', () => {
		submenuItems[i].style.height = `${heightArray[i]}px`;
	});
	li.addEventListener('mouseout', () => {
		submenuItems[i].style.height = '0px';
	});
});

$(".termSubmitBtn").on("click", function(e) { /*회원가입약관체크*/
	e.preventDefault();
	if (!$("input[name=termAgree]").prop("checked")) {
		alert("이용약관에 동의하셔야 회원가입을 하실 수 있습니다.");
		return;
	}
	location.href = $(this).attr("href");
});

$(".termCancelBtn").on("click", function(e) { /*회원가입 약관 이전버튼*/
	e.preventDefault();
	let previousUrl = document.referrer || $(this).attr("href");
	location.href = previousUrl;
});

$("form[name=loginForm]").on("submit", function(e) { /*login check*/
	e.preventDefault();
	if (!$(this).find("input[name=userId]").val().trim()) { alert("아이디를 입력하세요"); return; }
	if (!$(this).find("input[name=userPassword]").val().trim()) { alert("비밀번호를 입력하세요"); return; }
	this.submit();
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


$(".deleteBoardBtn").on("click", function(e) { /*게시글 삭제*/
	e.preventDefault();
	if (!confirm("정말로 삭제 하시겠습니까?")) { return; }
	$(".thumbnailUl > li").each((i, li) => {
		let file = { uuid: li.dataset.uuid, uploadPath: li.dataset.uploadpath, fileName: li.dataset.filename, fileType: li.dataset.filetype };
		console.log(file);
		fileService.remove(file);
	});
	location.href = $(this).attr("href");
});


function createFileInputs($form) {
	let html = "";
	$(".thumbnailUl > li").each((i, li) => {
		html += `<input type="hidden" name="files[${i}].uuid" value="${li.dataset.uuid}" />`;
		html += `<input type="hidden" name="files[${i}].uploadPath" value="${li.dataset.uploadpath}" />`;
		html += `<input type="hidden" name="files[${i}].fileName" value="${li.dataset.filename}" />`;
		html += `<input type="hidden" name="files[${i}].fileType" value="${li.dataset.filetype}" />`;
	});
	$form.append(html);
	$form.submit();
}

$("a.writeBoardBtn").on("click", function(e) { /*게시글 등록*/
	e.preventDefault();
	let formName = $(this).data("formname");
	const $form = $(`form.${formName}`);
	if (!$form.find("input[name=boardTitle]").val().trim()) { alert("제목을 입력하세요"); return; }
	if (!$form.find("textarea[name=boardContent]").val().trim()) { alert("내용을 입력하세요"); return; }
	createFileInputs($form);
});



$("a.updateBoardBtn").on("click", function(e) { /*게시글 수정*/
	e.preventDefault();
	let formName = $(this).data("formname");
	const $form = $(`form.${formName}`);
	if (!$form.find("input[name=boardTitle]").val().trim()) { alert("제목을 입력하세요"); return; }
	if (!$form.find("textarea[name=boardContent]").val().trim()) { alert("내용을 입력하세요"); return; }
	createFileInputs($form);
});


$("form[name=passwordCheckForm]").on("submit", function(e) { /*passworCheck password submit*/
	e.preventDefault();
	if (!$(this).find("input[name=userPassword]").val().trim()) { alert("비밀번호를 입력하세요"); return; }
	this.submit();
})



$("header > h1").on("click", function(e) { /*로고클릭시 메인화면으로*/
	e.preventDefault();
	location.href = contextPath + "/main";
})

$(".footer-job").on("click", function(e) { /*footer 채용안내*/
	e.preventDefault();
	alert("채용 기간이 아닙니다.");
});


$(".footer-partnership").on("click", function(e) {
	e.preventDefault();
	alert("서비스 준비중 입니다.");
});