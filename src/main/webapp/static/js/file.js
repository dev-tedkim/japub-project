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
let totalSize = 0;
let totalCount = 0;
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
$fileInput.on("change", function() {
	const formData = new FormData();
	const maxCount = 2;
	const files = Array.from($fileInput[0].files);
	if (!validateFileType($fileInput, files, category)) { refreshFileInput($fileInput, fileArray); console.log("들어옴"); return; }
	totalCount = fileArray.length + files.length;
	if (boardNum) { fileService.getCount(boardNum, count => totalCount += count); }
	if (totalCount > maxCount) { alert("파일은 최대 2개까지만 업로드할 수 있습니다."); refreshFileInput($fileInput, fileArray); return; }
	files.forEach(file => {
		if (!validateFile(file.name, file.size)) { return false; }
		fileArray.push(file);
		fileSizeArray.push(file.size);
		formData.append("multipartFiles", file);
	});
	isUpdate ? fileService.upload(formData, category, files => appendThumbnails({ $ul: $thumbnailUl, files, isUpdate: true })) : fileService.upload(formData, category, files => appendThumbnails({ $ul: $thumbnailUl, files }));
	refreshFileInput($fileInput, fileArray);
});


function validateFileType($fileInput, files, category) {
	const imageOnlyCategory = "free";
	if (!category || imageOnlyCategory != category) { return true; }
	if (!files || files.length == 0) { return; }
	for (let file of files) {
		const isImage = file.type.startsWith("image/");
		if (!isImage) {
			alert("이미지 파일만 업로드할 수 있습니다.");
			$fileInput.val("");
			return false;
		}
	}
	return true;
}

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

	if (index != -1) {
		refreshFileInput($fileInput, fileArray, index);
		$li.remove();
	} else {
		removeFileSize(fileSizeArray, index);
		$li.attr("class", deleteClassName).hide(); /*용량제거 필요*/
	}
});

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
	const maxSize = 1024 * 1024 * 20;
	totalSize += fileSize;
	if (maxSize < totalSize) {
		totalSize -= fileSize;
		return false;
	}
	return true;
}

function validateFile(fileName, fileSize) {
	if (!validateFileSize(fileSize)) {
		alert("업로드 가능한 용량을 초과하였습니다.");
		return false;
	}
	let regExp = new RegExp("(.*/)\.(exe|sh|alz)$", "i");
	if (regExp.test(fileName)) {
		alert("업로드 가능한 파일 형식이 아닙니다.");
		return false;
	}
	return true;
}
