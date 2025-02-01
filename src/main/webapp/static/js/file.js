let fileService = (function() {
	function upload(formData, callback) {
		$.ajax({
			url: `${contextPath}/files/upload`,
			method: 'post',
			contentType: false,
			processData: false,
			data: formData,
			success: function(files) {
				if (callback) {
					callback(files);
				}
			}
		});
	}
	function getFiles(boardNum, callback) {
		$.getJSON(`${contextPath}/files/${boardNum}`, function(files) {
			if (callback) {
				callback(files);
			}
		})
	}
	function remove(boardNum, file) {
		$.ajax({
			url: `${contextPath}/files/${boardNum}`,
			method: 'delete',
			contentType: 'application/json;charset=UTF-8',
			data: JSON.stringify(file),
			success: function() {
				console.log("성공");
			}
		});
	}
	return { upload: upload, getFiles: getFiles, remove: remove };
})();


$("input[name=multipartFiles]").on("change", function() {
	$(".thumbnailUl").empty();
	let files = $(this)[0].files;
	let formData = new FormData();
	Array.from(files).forEach(file => {
		if (!validateFile(file.name, file.size)) {
			return false;
		}
		formData.append("multipartFiles", file);
	});
	fileService.upload(formData, (files) => {
		appendThumbnails(files);
	});
});

$("a.writeBoardBtn").on("click", function(e) {
	e.preventDefault();
	let formName = $(this).data("formname");
	const $form = $(`form.${formName}`);
	if (!validateForm($form)) { return; }
	let html = createFileInput($(".thumbnailUl > li"));
	$form.append(html).submit();
});

function updateBoard(boardNum, files) {
	$("a.updateBoardBtn").on("click", function(e) {
		e.preventDefault();
		files.forEach(file => fileService.remove(boardNum, file));
		let formName = $(this).data("formname");
		const $form = $(`form.${formName}`);
		if (!validateForm($form)) { return; }
		let html = createFileInput($(".thumbnailUl > li"));
		$form.append(html).submit();
	});
}

function validateForm($form) {
	if (!$form.find("input[name=boardTitle]").val().trim()) { alert("제목을 입력하세요"); return false; }
	if (!$form.find("textarea[name=boardContent]").val().trim()) { alert("내용을 입력하세요"); return false; }
	return true;
}

$(".deleteBoardBtn").on("click", function(e) {
	e.preventDefault();
	if (!confirm("정말로 삭제 하시겠습니까?")) { return; }
	let files = getFiles($(".thumbnailUl > li"));
	files.forEach(file => fileService.remove(boardNum, file));
	location.href = $(this).attr("href");
});




function getFiles($items) {
	let files = new Array();
	$items.each((i, li) => {
		files[i] = { uuid: `${li.dataset.uuid}`, uploadPath: `${li.dataset.uploadpath}`, fileName: `${li.dataset.filename}`, fileType: `${li.dataset.filetype}` };
	});
	return files;
}


function appendThumbnails(files, isDownload = false) {
	const $thumbnailUl = $(".thumbnailUl");
	let html = createThumbnailHtml(files, isDownload);
	$thumbnailUl.empty().append(html)
}


function createFileInput($liElements) {
	let html = "";
	$liElements.each((i, li) => {
		html += `<input type="hidden" name="files[${i}].uuid" value="${li.dataset.uuid}" />`;
		html += `<input type="hidden" name="files[${i}].uploadPath" value="${li.dataset.uploadpath}" />`;
		html += `<input type="hidden" name="files[${i}].fileName" value="${li.dataset.filename}" />`;
		html += `<input type="hidden" name="files[${i}].fileType" value="${li.dataset.filetype}" />`;
	});
	return html;
}


function createThumbnailHtml(files, isDownload = false) {
	let html = "";
	files.forEach(file => {
		let displayFileName = encodeURIComponent(`${file.uploadPath}/t_${file.uuid}_${file.fileName}`);
		let downloadFileName = displayFileName.replace("t_", "");
		html += `<li data-uuid="${file.uuid}" data-uploadpath="${file.uploadPath}" data-filename="${file.fileName}" data-filetype="${file.fileType}">`;
		html += isDownload ? `<a href="${contextPath}/files/download?fileName=${downloadFileName}">` : ``;
		html += file.fileType ? `<img src="${contextPath}/files/display?fileName=${displayFileName}" width="100" />` : `<img src="${contextPath}/static/image/attach.png" width="100" />`;
		html += `</li>`;
		html += isDownload ? `</a>` : ``;
	});
	return html;
}

function validateFile(fileName, fileSize) {
	let maxSize = 1024 * 1024 * 100;
	let regExp = new RegExp("(.*/)\.(exe|sh|zip|alz)$", "i");
	if (regExp.test(fileName)) {
		alert("지원하지 않는 파일 형식입니다.");
		return false;
	}
	if (fileSize > maxSize) {
		alert("업로드 가능한 용량이 초과하였습니다.");
		return false;
	}
	return true;
}