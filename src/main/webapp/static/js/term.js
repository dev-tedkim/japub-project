$(".termSubmitBtn").on("click", function(e) { /*회원가입약관체크*/
	e.preventDefault();
	if (!$("input[name=termAgree]").prop("checked")) {
		alert("이용약관에 동의하셔야 회원가입이 가능합니다.");
		return;
	}
	$("form[name=termForm]").submit();
});

$(".termCancelBtn").on("click", function(e) { /*회원가입 약관 이전버튼*/
	e.preventDefault();
	let previousUrl = document.referrer || $(this).attr("href");
	location.href = previousUrl;
});