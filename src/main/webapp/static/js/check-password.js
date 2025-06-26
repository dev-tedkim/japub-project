$("input.pw-check-btn").on("click", function(e) {
	e.preventDefault();
	if (!$(this).prev().val().trim()) { alert("비밀번호를 입력해주세요."); return; }
	$(this).closest("form").submit();
});