window.boardNum = $("#board-container").data("boardNum");
const $thumbnailUl = $(".thumbnailUl");
const fileArray = new Array();
const $fileInput = $("input[name=multipartFiles]");
const fileService = (function() {
	function upload(formData, callback) {
		$.ajax({
			url: `${contextPath}/files/upload`,
			method: 'post',
			contentType: false,
			processData: false,
			data: formData,
			success: callback
		});
	}

	function getFiles(boardNum, callback) {
		$.ajax({
			url: `${contextPath}/files/${boardNum}`,
			method: 'get',
			success: callback
		});
	}

	function getSize(boardNum) {
		let result;
		$.ajax({
			url: `${contextPath}/files/size/${boardNum}`,
			method: 'get',
			async: false,
			success: function(size) { result = size; }
		});
		return result;
	}

	return { upload, getFiles, getSize };
})();
let totalFileSize = 0;

$fileInput.on("change", function() {
	const maxSize = 3;
	const files = Array.from($fileInput[0].files);
	const formData = new FormData();
	const isUpdate = $fileInput.data("update");
	let size = boardNum ? fileService.getSize(boardNum) : 0;
	size = size + fileArray.length + files.length;
	if (size > maxSize) {
		alert("파일은 최대 3개까지만 업로드할 수 있습니다.");
		$fileInput.val("");
		resetFileInput($fileInput, fileArray);
		return;
	}
	files.forEach(file => {
		if (!validateFile(file.name, file.size)) { return false; }
		formData.append("multipartFiles", file);
		fileArray.push(file);
	});
	if (isUpdate) {
		fileService.upload(formData, files => appendThumbnails({ $ul: $thumbnailUl, files, isUpdate: true }));
	} else {
		fileService.upload(formData, files => appendThumbnails({ $ul: $thumbnailUl, files }));
	}
	resetFileInput($fileInput, fileArray);
});

$thumbnailUl.on("click", ".file-cancel-btn", function(e) {
	e.preventDefault();
	const $li = $(this).closest("li");
	const isUpdate = $fileInput.data("update");
	let index;
	if (!isUpdate) {
		index = $(".file-cancel-btn").index($(this));
		resetFileInputByIndex($fileInput, fileArray, index);
		$li.remove();
		return;
	}
	index = $("li.new").index($li);
	if (index != -1) {
		resetFileInputByIndex($fileInput, fileArray, index);
		$li.remove();
	} else {
		$li.attr("class", "remove").hide();
	}
});

function resetFileInput($fileInput, fileArray) {
	const dataTransfer = new DataTransfer();
	fileArray.forEach(file => dataTransfer.items.add(file));
	$fileInput[0].files = dataTransfer.files;
}

function resetFileInputByIndex($fileInput, fileArray, index) {
	const dataTransfer = new DataTransfer();
	fileArray.splice(index, 1);
	fileArray.forEach(file => dataTransfer.items.add(file));
	$fileInput[0].files = dataTransfer.files;
}

function showThumbnails($ul, boardNum, isDownload) {
	fileService.getFiles(boardNum, files => appendThumbnails({ $ul, files, isDownload }));
}

function createFileInfoInputs($ul, className) {
	const $liElements = $ul.find(`li.${className}`);
	let html = "";
	let name = className == 'remove' ? 'deleteFiles' : 'files';
	$liElements.each((i, li) => {
		html += `<input type="hidden" name="${name}[${i}].fileNum" value="${li.dataset.fileNum}" />`;
		html += `<input type="hidden" name="${name}[${i}].fileUuid" value="${li.dataset.fileUuid}" />`;
		html += `<input type="hidden" name="${name}[${i}].fileUploadPath" value="${li.dataset.uploadPath}" />`;
		html += `<input type="hidden" name="${name}[${i}].fileName" value="${li.dataset.fileName}" />`;
		html += `<input type="hidden" name="${name}[${i}].fileType" value="${li.dataset.fileType}" />`;
		html += `<input type="hidden" name="${name}[${i}].fileSize" value="${li.dataset.fileSize}" />`;
	});
	return html;
}

function appendThumbnails({ $ul, files, isDownload, isUpdate }) {
	let html = createThumbnails(files, isDownload, isUpdate);
	$ul.append(html);
}

function createThumbnails(files, isDownload = false, isUpdate = false) {
	let html = "";
	files.forEach(file => {
		let displayFileName = encodeURIComponent(`${file.fileUploadPath}/${file.fileUuid}_${file.fileName}`);
		let downloadFileName = displayFileName.replace("t_", "");
		let fileNum = `${file.fileNum == null ? '' : file.fileNum}`;
		let className = isUpdate ? 'new' : 'original';
		html += `<li class="${className}" data-file-num="${fileNum}" data-file-uuid="${file.fileUuid}" data-upload-path="${file.fileUploadPath}" data-file-name="${file.fileName}" data-file-size="${file.fileSize}" data-file-type="${file.fileType}">`;
		html += isDownload ? `<a href="${contextPath}/files/download?fileName=${downloadFileName}">` : ``;
		html += file.fileType ? `<img src="${contextPath}/files/display?fileName=${displayFileName}" width="100" />` : `<img src="${contextPath}/static/images/file/attach.png" width="100" />`;
		html += isDownload ? `</a>` : ``;
		html += !isDownload ? `<img class="file-cancel-btn" src="${contextPath}/static/images/file/cancel.png" width="25" />` : ``;
		html += `</li>`;
	});
	return html;
}












function validateFileSize(fileSize) {
	const maxSize = 1024 * 1024 * 200;
	totalFileSize += fileSize;
	if (maxSize < totalFileSize) {
		alert("업로드 가능한 용량을 초과하였습니다.");
		totalFileSize -= fileSize;
		return false;
	}
	return true;
}

function validateFile(fileName, fileSize) {
	let regExp = new RegExp("\\.(exe|sh|alz)$", "i");
	if (regExp.test(fileName)) {
		alert("업로드 가능한 파일 형식이 아닙니다.");
		return false;
	}
	if (!validateFileSize(fileSize)) {
		return false;
	}
	return true;
}


/*function validateFile(fileName, fileSize) {
	let maxSize = 1024 * 1024 * 350;
	let regExp = new RegExp("\\.(exe|sh|alz)$", "i");
	if (maxSize < fileSize) {
		alert("업로드 가능한 용량을 초과하였습니다.");
		return false;
	}
	if (regExp.test(fileName)) {
		alert("업로드 가능한 파일 형식이 아닙니다.");
		return false;
	}
	return true;
}*/


