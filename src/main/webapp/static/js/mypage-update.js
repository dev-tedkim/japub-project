const $passwordInput = $("input[name=userPassword]");
const $confirmPasswordInput = $("input[name=userPasswordCheck]");
let password = "";

$("input.changePwBtn").on("click", function(e) {
	e.preventDefault();
	$("input.cancelChangePwBtn").show();
	$(this).hide().next().show();
	password = $passwordInput.val();
	$confirmPasswordInput.attr("type", "password");
	$passwordInput.prop("readonly", false).val("").focus();
});

$("input.cancelChangePwBtn").on("click", function(e) {
	e.preventDefault();
	$(this).hide();
	$("input.changePwBtn").show().next().hide();
	$passwordInput.prop("readonly", true).val(password);
	password = "";
	$confirmPasswordInput.val("").attr("type", "hidden");
	changeCss($passwordInput, true);
	changeCss($confirmPasswordInput, true);
	resetPasswordCheck($passwordInput, $confirmPasswordInput);
});

$("input.changePwOkbtn").on("click", function(e) {
	e.preventDefault();
	const { userPassword, userPasswordCheck } = getPasswordChecks();
	if (!emptyCheck()) { return; }
	if (!userPassword || !userPasswordCheck) { alert("모든 항목을 정확히 입력하세요"); return; }
	$(this).closest("form").submit();
});

