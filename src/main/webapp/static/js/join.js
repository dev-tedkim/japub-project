function sample6_execDaumPostcode() {
	new daum.Postcode({
		oncomplete: function(data) {
			// 팝업에서 검색결과 항목을 클릭했을때 실행할 코드를 작성하는 부분.

			// 각 주소의 노출 규칙에 따라 주소를 조합한다.
			// 내려오는 변수가 값이 없는 경우엔 공백('')값을 가지므로, 이를 참고하여 분기 한다.
			var addr = ''; // 주소 변수
			var extraAddr = ''; // 참고항목 변수

			//사용자가 선택한 주소 타입에 따라 해당 주소 값을 가져온다.
			if (data.userSelectedType === 'R') {
				// 사용자가 도로명 주소를 선택했을 경우
				addr = data.roadAddress;
			} else {
				// 사용자가 지번 주소를 선택했을 경우(J)
				addr = data.jibunAddress;
			}

			// 사용자가 선택한 주소가 도로명 타입일때 참고항목을 조합한다.
			if (data.userSelectedType === 'R') {
				// 법정동명이 있을 경우 추가한다. (법정리는 제외)
				// 법정동의 경우 마지막 문자가 "동/로/가"로 끝난다.
				if (data.bname !== '' && /[동|로|가]$/g.test(data.bname)) {
					extraAddr += data.bname;
				}
				// 건물명이 있고, 공동주택일 경우 추가한다.
				if (data.buildingName !== '' && data.apartment === 'Y') {
					extraAddr += extraAddr !== '' ? ', ' + data.buildingName : data.buildingName;
				}
				// 표시할 참고항목이 있을 경우, 괄호까지 추가한 최종 문자열을 만든다.
				if (extraAddr !== '') {
					extraAddr = ' (' + extraAddr + ')';
				}
			}

			// 우편번호와 주소 정보를 해당 필드에 넣는다.
			document.getElementById('sample6_postcode').value = data.zonecode;
			document.getElementById('sample6_address').value = addr;
			// 커서를 상세주소 필드로 이동한다.
			document.getElementById('sample6_detailAddress').focus();
		},
	}).open();
}

let validationChecks =
{
	userId: false,
	userPassword: false,
	userPasswordCheck: false,
	userPhoneNumber: false,
	userEmail: false
};

let joinService = (function() {
	function checkId(id, callback) { /*아이디중복체크 ajax*/
		$.ajax({
			url: `${contextPath}/user/checkId`,
			method: 'POST',
			contentType: 'application/json;charset=UTF-8',
			data: JSON.stringify(id),
			success: function(isSuccess) {
				if (callback) {
					callback(isSuccess);
				}
			}
		});
	}

	function checkEmail(email, callback) { /*이메일중복체크 ajax*/
		$.ajax({
			url: `${contextPath}/user/checkEmail`,
			method: 'POST',
			contentType: 'application/json;charset=UTF-8',
			data: JSON.stringify(email),
			success: function(isSuccess) {
				if (callback) {
					if (callback) {
						callback(isSuccess);
					}
				}
			}
		});
	}
	return { checkId: checkId, checkEmail: checkEmail }
})();



$(".joinInputDiv > input.submit").on("click", function(e) { /*submit*/
	e.preventDefault();
	if (!emptyCheck()) { return; }
	if (Object.values(validationChecks).includes(false)) {
		alert("모든 정보를 정확히 입력해주세요.");
		return;
	}
	$("form[name=joinForm]").submit();
});

$("input[name=userId]").on("blur", function() {  /*userId 유효성*/
	let $input = $(this);
	let id = $(this).val().trim();
	if (!id || $(this).prop("readonly")) { return; }
	let isSuccess = validateId(id);
	let msg = '5~20자의 영문 소문자, 숫자와 _ - 만 사용 가능합니다.';
	showMessage($(this), isSuccess, msg);
	setvalidationCheck($(this), isSuccess);
	if (!isSuccess) { return; } /*유효성검사 실패시 중복체크없이 바로 return*/
	joinService.checkId({ userId: id }, (isSuccess) => {
		let msg = isSuccess ? '사용 가능한 아이디 입니다.' : '사용할 수 없는 아이디 입니다.';
		showMessage($input, isSuccess, msg);
		$input.next("span").css("color", `${isSuccess ? 'green' : 'red'}`).show(); /*일회성으로 span에 스타일 줘야됌*/
		setvalidationCheck($input, isSuccess);
	});
});


$("input[name=userPassword]").on("blur", function() {  /*password 유효성*/
	let password = $(this).val().trim();
	if (!password || $(this).prop("readonly")) { return; }
	let isSuccess = validatePassword(password);
	let msg = '8~16자의 영문 대/소문자, 숫자를 사용해 주세요.'; /*실패시 메세지*/
	showMessage($(this), isSuccess, msg);
	setvalidationCheck($(this), isSuccess);
	if (isSuccess) { $("input[name=userPasswordCheck]").trigger("blur"); }
});


$("input[name=userPasswordCheck]").on("blur", function() {  /*passwordCheck 유효성*/
	let passwordCheck = $(this).val().trim();
	let password = $("input[name=userPassword]").val().trim();
	if (!passwordCheck || !password) { return; }
	let isSuccess = password == passwordCheck;
	let msg = "비밀번호가 일치하지 않습니다.";
	showMessage($(this), isSuccess, msg);
	setvalidationCheck($(this), isSuccess);
});


$("input[name=userPhoneNumber]").on("blur", function() { /*phone 유효성*/
	let phone = $(this).val().trim();
	if (!phone || $(this).prop("readonly")) { return; }
	let isSuccess = validatePhone(phone);
	let msg = "핸드폰번호가 정확한지 확인해 주세요.";
	showMessage($(this), isSuccess, msg);
	setvalidationCheck($(this), isSuccess);
});

$("input[name=userEmail]").on("blur", function() { /*userEmail 유효성*/
	let $input = $(this);
	let email = $(this).val().trim();
	if (!email || $(this).prop("readonly")) { return; }
	let isSuccess = validateEmail(email);
	let msg = '이메일 주소가 정확한지 확인해 주세요.';
	showMessage($(this), isSuccess, msg);
	setvalidationCheck($(this), isSuccess);
	if (!isSuccess) { return; }
	joinService.checkEmail({ userEmail: email }, (isSuccess) => {
		msg = "이미 가입된 이메일 입니다."
		showMessage($input, isSuccess, msg);
		setvalidationCheck($input, isSuccess);
	});
});



function setvalidationCheck($ele, isSuccess) {
	if (!isSuccess) { return; }
	let name = $ele.attr("name");
	validationChecks[name] = true;
}

function showMessage($ele, isSuccess, msg) { /*span.errorMsg set css*/
	let color = isSuccess ? 'rgb(235, 235, 235)' : 'red';
	const $span = $ele.next("span");
	isSuccess ? $ele.css("outlineColor", color) : $ele.css("outlineColor", color);
	isSuccess ? $ele.css("borderColor", color) : $ele.css("borderColor", color);
	isSuccess ? $span.text(msg).hide() : $span.text(msg).show();
}






function validateId(id) { /*아이디 정규식*/
	const regex = /^[a-z0-9_-]{5,20}$/;
	if (!regex.test(id)) {
		return false;
	}
	return true;
}

function validatePassword(password) { /*비밀번호 정규식*/
	const regex = /^(?=.*[a-zA-Z])(?=.*\d)[a-zA-Z\d]{7,16}$/;
	if (!regex.test(password)) {
		return false;
	}
	return true;
}

function validateEmail(email) { /*이메일정규식*/
	const regex = /^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,4}$/i;
	if (!regex.test(email)) {
		return false;
	}
	return true;
}

function validatePhone(phoneNumber) {  /*핸드폰정규식*/
	const regex = /^(01[0-9]{1})-?([0-9]{3,4})-?([0-9]{4})$/;
	if (!regex.test(phoneNumber)) {
		return false;
	}
	return true;
}




function emptyCheck() { /*join input empty check*/
	if (!$("input[name = userId]").val().trim()) {
		alert("아이디를 입력해주세요.");
		return false;
	}
	if (!$("input[name = userPassword]").val().trim()) {
		alert("비밀번호를 입력해주세요.");
		return false;
	}
	if (!$("input[name = userPasswordCheck]").val().trim()) {
		alert("비밀번호를 한번더 입력해주세요.");
		return false;
	}
	if (!$("input[name = userPhoneNumber]").val().trim()) {
		alert("핸드폰번호를 입력해주세요.");
		return false;
	}
	if (!$("input[name = userEmail]").val().trim()) {
		alert("이메일을 입력해주세요.");
		return false;
	}
	return true;
}