window.boardNum = $("#board-container").data("boardNum");
const $fileInput = $("input[name=multipartFiles]");
const $thumbnailUl = $("ul.thumbnail-ul");
const fileArray = [];
const fileSizeArray = [];
const deleteClassName = "remove";
const updateClassName = "new";
const defaultClassName = "original";
const isUpdate = $fileInput.data("update");
const category = $("div.container").data("boardCategory");
const fileService = (function() {
	function upload(formData, category, callback) {
		$.ajax({
			url: `${contextPath}/files/upload?category=${category}`,
			method: 'post',
			data: formData,
			contentType: false,
			processData: false,
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

	function getCount(boardNum, callback) {
		$.ajax({
			url: `${contextPath}/files/count/${boardNum}`,
			method: 'get',
			async: false,
			success: callback
		});
	}

	return { upload, getFiles, getCount };
})();
let totalSize = 0;
let totalCount = 0;
let removeFileCount = 0;

$fileInput.on("change", function() {
	const maxCount = 2;
	const files = Array.from($fileInput[0].files);
	const formData = new FormData();
	totalCount = removeFileCount + fileArray.length + files.length;
	if (boardNum) { fileService.getCount(boardNum, count => totalCount += count); }
	if (totalCount > maxCount) { alert("파일은 최대 2개까지만 업로드할 수 있습니다."); refreshFileInput($fileInput, fileArray); return; }
	files.forEach(file => {
		if (!validateFileSize(file.size)) { return false; }
		if (!validateFileType(file.type, category)) { return false; }
		if (!validateFileName(file.name)) { return false; }
		formData.append("multipartFiles", file);
		fileArray.push(file);
		fileSizeArray.push(file.size);
	});
	const isEmpty = formDataIsEmpty(formData);
	if (!isEmpty) {
		isUpdate ? fileService.upload(formData, category, files => appendThumbnails({ $ul: $thumbnailUl, files, isUpdate: true })) : fileService.upload(formData, category, files => appendThumbnails({ $ul: $thumbnailUl, files }));
	}
	refreshFileInput($fileInput, fileArray);
});

$thumbnailUl.on("click", ".file-cancel-btn", function(e) {
	e.preventDefault();
	const $li = $(this).closest("li");
	let index;

	if (!isUpdate) {/*용량제거 필요*/
		index = $("img.file-cancel-btn").index($(this));
		refreshFileInput($fileInput, fileArray, index);
		removeFileSize(fileSizeArray, index)
		$li.remove();
		return;
	}

	index = $(`li.${updateClassName}`).index($li);

	if (index != -1) {/*용량제거 필요*/
		refreshFileInput($fileInput, fileArray, index);
		removeFileSize(fileSizeArray, index);
		$li.remove();
	} else {
		$li.attr("class", deleteClassName).hide();
		--removeFileCount;
	}
});

function formDataIsEmpty(formData) {
	for (let entries of formData.entries()) {
		return false;
	}
	return true;
}

function removeFileSize(fileSizeArray, index) {
	totalSize -= fileSizeArray[index];
	fileSizeArray.splice(index, 1);
}

function getDeleteClassName() {
	return deleteClassName;
}

function getUpdateClassName() {
	return updateClassName;
}

function showThumbnails($ul, boardNum, isDownload) {
	fileService.getFiles(boardNum, (files) => appendThumbnails({ $ul, files, isDownload }));
}

function appendThumbnails({ $ul, files, isDownload, isUpdate }) {
	let html = createThumbnails(files, isDownload, isUpdate);
	$ul.append(html);
}

function refreshFileInput($fileInput, fileArray, index = -1) {
	const dataTransfer = new DataTransfer();
	if (index >= 0) { fileArray.splice(index, 1); }
	fileArray.forEach(file => dataTransfer.items.add(file));
	$fileInput[0].files = dataTransfer.files;
}

function createHiddenInputs($ul, className = defaultClassName) {
	const name = className == deleteClassName ? "deleteFiles" : "insertFiles";
	let html = "";
	$ul.find(`li.${className}`).each((i, li) => {
		html += `<input type="hidden" name="${name}[${i}].fileNum" value="${li.dataset.fileNum}" />`;
		html += `<input type="hidden" name="${name}[${i}].fileUuid" value="${li.dataset.fileUuid}" />`;
		html += `<input type="hidden" name="${name}[${i}].fileUploadPath" value="${li.dataset.fileUploadPath}" />`;
		html += `<input type="hidden" name="${name}[${i}].fileName" value="${li.dataset.fileName}" />`;
		html += `<input type="hidden" name="${name}[${i}].fileSize" value="${li.dataset.fileSize}" />`;
		html += `<input type="hidden" name="${name}[${i}].fileType" value="${li.dataset.fileType}" />`;
	});
	return html;
}

function createThumbnails(files, isDownload = false, isUpdate = false) {
	let html = "";
	files.forEach(file => {
		const displayFilePath = encodeURIComponent(`${file.fileUploadPath}/t_${file.fileUuid}_${file.fileName}`);
		const downloadFilePath = displayFilePath.replace("t_", "");
		const fileNum = file.fileNum ? file.fileNum : "";
		const className = isUpdate ? updateClassName : defaultClassName;
		html += `<li class="${className}" data-file-num="${fileNum}" data-file-uuid="${file.fileUuid}" data-file-upload-path="${file.fileUploadPath}" data-file-name="${file.fileName}" data-file-size="${file.fileSize}" data-file-type="${file.fileType}">`;
		html += isDownload ? `<a href="${contextPath}/files/download?filePath=${downloadFilePath}&category=${category}">` : ``;
		html += file.fileType ? `<img src="${contextPath}/files/display?filePath=${displayFilePath}&category=${category}" width="100" />` : `<img src="${contextPath}/static/images/file/attach.png" width="100" />`;
		html += isDownload ? `</a>` : ``;
		html += !isDownload ? `<img class="file-cancel-btn" src="${contextPath}/static/images/file/cancel.png" width="25" />` : ``;
		html += `</li>`;
	});
	return html;
}

function validateFileSize(fileSize) {
	const maxFileSize = 1024 * 1024 * 200;
	totalSize += fileSize;
	if (maxFileSize < totalSize) {
		totalSize -= fileSize;
		alert("업로드 가능한 용량을 초과하였습니다.");
		return false;
	}
	return true;
}

function validateFileType(fileType, category) {
	const onlyImageCategory = "free";
	if (category != onlyImageCategory) {
		return true;
	}
	const isImage = fileType.startsWith("image/");
	if (!isImage) {
		alert("이미지만 업로드 가능합니다.");
		return false;
	}
	return true;
}

function validateFileName(fileName) {
	let regExp = new RegExp("(.*/)\.(exe|sh|alz)$", "i");
	if (regExp.test(fileName)) {
		alert("업로드 가능한 파일 형식이 아닙니다.");
		return false;
	}
	return true;
}
