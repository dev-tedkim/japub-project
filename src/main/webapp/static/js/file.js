let fileService = (function() {
	function upload(formData) {
		$.ajax({
			url: `${contextPath}/files`,
			method: "post",
			contentType: false,
			processData: false,
			data: formData,
			dataType: "json",
			success: function(files) {
				createThumbnail(files);
			}
		});
	}

	function getFiles() {
		$.ajax({
			url: `${contextPath}/files/${boardNum}`,
			method: "get",
			async: false,
			success: function(files) {
				createThumbnail(files, true);
			}
		})
	}

	function remove(file) {
		$.ajax({
			url: `${contextPath}/files/${boardNum}`,
			method: "DELETE",
			contentType: "application/json;charset=UTF-8",
			data: JSON.stringify(file),
		});
	}
	return { upload: upload, getFiles: getFiles, remove: remove };
})();






$("input[name=multipartFiles]").on("change", function() { /*업로드*/
	let files = $(this)[0].files;
	let formData = new FormData();
	Array.from(files).forEach(file => {
		if (!fileValidate(file.name, file.size)) {
			return false;
		}
		formData.append("multipartFiles", file);
	});
	fileService.upload(formData);
});





function fileValidate(name, size) { /*파일검증*/
	const maxSize = 1024 * 1024 * 100;
	let regExp = new RegExp("(.*/)\.(exe|sh|zip|alz)$", "i");
	if (size > maxSize) {
		alert("업로드 가능한 파일 사이즈를 초과 하였습니다.");
		return false;
	}
	if (regExp.test(name)) {
		alert("업로드 가능한 파일 형식이 아닙니다.");
		return false;
	}
	return true;
}


function createThumbnail(files, isDownload = false) { /*썸네일생성*/
	let html = "";
	files.forEach(file => {
		let fileName = file.type ? `${file.uploadPath}/t_${file.uuid}_${file.fileName}` : `${file.uploadPath}/${file.uuid}_${file.fileName}`;
		html += `<li data-uuid="${file.uuid}"  data-uploadpath="${file.uploadPath}" data-filename="${file.fileName}" data-filetype="${file.fileType}">`;
		isDownload ? html += `<a href="${contextPath}/files/download?fileName=${encodeURIComponent(fileName)}">` : '';
		file.fileType ? html += `<img src="${contextPath}/files/display?fileName=${encodeURIComponent(fileName)}" width="100"/>` : html += `<img src="${contextPath}/static/image/attach.png" width="100"/>`;
		isDownload ? html += `</a>` : '';
		html += `</li>`;
	});
	$("ul.thumbnailUl").empty().append(html);
}