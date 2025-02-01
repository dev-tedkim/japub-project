fileService.getFiles(boardNum,(files) => {
	appendThumbnails(files);
	files = getFiles($(".thumbnailUl > li"));
	updateBoard(boardNum,files);
});