let findUser = function(email) {
	$.ajax({
		url: `${contextPath}/user/find`,
		method: 'post',
		contentType: 'application/json;charset=UTF-8',
		data: JSON.stringify(email),
		success: function(msg) {
			alert(msg);
		}
	});
}


$(".find-footer a.find-btn").on("click", function(e) {
	e.preventDefault();
	alert("처리중입니다 잠시만 기다려주세요");
	const $form = $("form[name=find-user-form]");
	let userEmail = $form.find("input[name=userEmail]").val().trim();
	if (!userEmail) { alert("이메일을 입력하세요"); return; }
	findUser({ userEmail: userEmail });
});