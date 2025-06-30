$("form[name=loginForm]").on("submit", function(e) { /*login check*/
	e.preventDefault();
	if (!$(this).find("input[name=userId]").val().trim()) { alert("아이디를 입력하세요"); return; }
	if (!$(this).find("input[name=userPassword]").val().trim()) { alert("비밀번호를 입력하세요"); return; }
	this.submit();
});