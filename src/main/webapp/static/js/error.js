$("a.back-btn").on("click", function(e) {
	e.preventDefault();
	if (history.length > 1) {
		history.back();
	} else {
		location.href = $(this).attr("href");
	}
});