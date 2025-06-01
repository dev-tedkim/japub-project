const findByUserEmail = (email) => {
	$.ajax({
		url: `${contextPath}/find-account`,
		method: 'post',
		contentType: 'application/json;charset=UTF-8',
		data: JSON.stringify(email),
		success: function(msg) {
			alert(msg);
		},
		error: function(xhr) {
			alert(xhr.responseText);
		}
	});
}

$("a.find-btn-right").on("click", function(e) {
	e.preventDefault();
	const $input = $(this).closest("div.find-container").find("input[name=userEmail]");
	let userEmail = $input.val().trim();
	if (!userEmail) { alert("이메일을 입력해 주세요."); return; }
	findByUserEmail({ userEmail });
	alert("요청이 완료되었습니다.\n잠시만 기다려 주세요.");
});