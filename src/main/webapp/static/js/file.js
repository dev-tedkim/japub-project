let fileService = (function() {
	function showThumbnail(formData) {
		$.ajax({
			url: `${contextPath}/files`,
			method: 'post',
			contentType: false,
			processData: false,
			data: formData,
			success: function(files) {
				createThumbnail(files);
			}
		});
	}
	function getFiles(isDownload) {
		$.getJSON(`${contextPath}/files/${boardNum}`, function(files) {
			console.log(files);
			createThumbnail(files, isDownload);
		});
	}
	return { showThumbnail: showThumbnail, getFiles: getFiles }
})();



$("input[name=multipartFiles]").on("change", function(e) {
	$(".thumbnailUl").empty();
	let formData = new FormData();
	let files = $(this)[0].files;
	Array.from(files).forEach(file => {
		if (!fileValidate(file.name, file.size)) {
			return false;
		}
		formData.append("multipartFiles", file);
	});
	fileService.showThumbnail(formData);
});



function createThumbnail(files, isDownload = false) {
	let html = "";
	files.forEach(file => {
		let fileName = `${file.uploadPath}/t_${file.uuid}_${file.fileName}`;
		html += `<li data-uuid="${file.uuid}" data-uploadpath="${file.uploadPath}" data-filename="${file.fileName}" data-filetype="${file.fileType}" >`;
		html += isDownload ? `<a href="${contextPath}/files/download?fileName=${encodeURIComponent(fileName.replace("t_", ""))}" >` : ``;
		html += file.fileType ? `<img src="${contextPath}/files/display?fileName=${encodeURIComponent(fileName)}" width="100" />` : `<img src="${contextPath}/static/image/attach.png" width="100" />`;
		html += isDownload ? `</a>` : ``;
		html += `</li>`;
	});
	$(".thumbnailUl").empty().append(html);
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