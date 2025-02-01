
$(".changePwBtn").on("click", function(e) { /* 마이페이지 비밀번호변경 */
	$(this).hide();
	$(this).next().show();
	$mypageForm.find(".cancelChangePwBtn").show();
	$passwordInput.prop("readonly", false);
	$passwordCheckInput.attr("type","password");
	$passwordInput.focus();
	userPassword = $passwordInput.val().trim();
	$passwordInput.val("");
});

$(".cancelChangePwBtn").on("click", function() {  /* 마이페이지 비밀번호변경 취소 */
	$(this).hide();
	$mypageForm.find(".changePwBtn").show().next().hide();
	$passwordInput.val(userPassword);
	$passwordInput.prop("readonly", true);
	$passwordCheckInput.attr("type","hidden").val("");
	showMessage($passwordInput,true);
	showMessage($passwordCheckInput,true);
	userPassword = "";	
});


$(".changePwOkbtn").on("click",function(e){ /*mypage submit check*/
	if(!emptyCheck()){return;} 
	const {userPassword, userPasswordCheck} = validationChecks;
	if(Object.values[userPassword, userPasswordCheck].includes(false)){
		alert("모든 항목을 정확히 입력해주세요.");
		return;
	}
	$mypageForm.submit();
});