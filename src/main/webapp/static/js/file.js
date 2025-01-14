let fileService = (function() {
	function showThumbnailImages(formData) {
		$.ajax({
			url: `${contextPath}/files`,
			method: 'post',
			data: formData,
			contentType: false,
			processData: false,
			success: function(files) {
				createThumbnailImages(files);
			}
		});
	}

	function getFiles(isDownload) {
		$.getJSON(`${contextPath}/files/${boardNum}`, function(files) { createThumbnailImages(files, isDownload); });
	}

	function remove(file) {
		$.ajax({
			url: `${contextPath}/files/${boardNum}`,
			method: 'delete',
			data: JSON.stringify(file),
			contentType:'application/json;charset=UTF-8',
			success: function() {
				console.log("file delete ok");
			}
		});
	}

	return { showThumbnailImages: showThumbnailImages, getFiles: getFiles, remove: remove }
})();




$("input[name=multipartFiles]").on("change", function() {
	$("ul.thumbnailUl").empty();
	let files = $(this)[0].files;
	let formData = new FormData();
	Array.from(files).forEach(file => {
		if (!fileValidate(file.name, file.size)) {
			return;
		}
		formData.append("multipartFiles", file);
	});
	fileService.showThumbnailImages(formData);
});


function createThumbnailImages(files, isDownload = false) {
	let html = "";
	files.forEach(file => {
		let thumbnailFileName = `${file.uploadPath}/t_${file.uuid}_${file.fileName}`;
		let downloadFileName = thumbnailFileName.replace("t_", "");
		html += `<li data-uuid="${file.uuid}" data-uploadpath="${file.uploadPath}" data-filename="${file.fileName}" data-filetype="${file.fileType}" >`;
		html += isDownload ? `<a href="${contextPath}/files/download?fileName=${encodeURIComponent(downloadFileName)}" >` : ``;
		html += file.fileType ? `<img src="${contextPath}/files/display?fileName=${encodeURIComponent(thumbnailFileName)}" width="100" />` : `<img src="${contextPath}/static/image/attach.png" width="100" />`;
		html += `</li>`;
		html += isDownload ? `</a>` : '';
	});
	$("ul.thumbnailUl").empty().append(html);
}




function fileValidate(name, size) {
	let regExp = new RegExp("(.*/)\.(exe|sh|zip|alz)$", "i");
	const maxSize = 1024 * 1024 * 100;
	if (regExp.test(name)) {
		alert("업로드 가능한 파일 형식이 아닙니다.");
		return false;
	}
	if (size > maxSize) {
		alert("업로드 가능한 파일 사이즈를 초과 하였습니다.");
		return false;
	}
	return true;
}