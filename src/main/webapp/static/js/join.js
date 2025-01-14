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

let validationChecks = { /*하나라도 false면 submit 불가*/
	userId: false,
	userPassword: false,
	userPasswordCheck: false,
	userEmail: false,
	userPhoneNumber: false
}

let joinValidate = (function() { /*id check ajax*/
	function checkId(id, callback) {
		$.ajax({
			url: `${contextPath}/user/checkId?userId=${id}`,
			method: 'get',
			success: function(isSuccess) {
				if (callback) {
					callback(isSuccess);
				}
			}
		});
	}
	return { checkId: checkId }
})();

$("form[name=joinForm]  input[name=userId]").on("blur", function() { /*id blur*/
	const $element = $(this).next();
	let id = $(this).val().trim();
	if (!id) { return; }
	let isSuccess = validateId(id);
	if (!isSuccess) {
		let msg = '5~20자의 영문 소문자, 숫자와 _ - 만 사용 가능합니다.';
		changeStyle($element, isSuccess, msg);
		validationChecks.userId = isSuccess;
		return;
	}
	joinValidate.checkId(id, (isSuccess) => {
		let msg = isSuccess ? '사용 가능한 아이디 입니다' : '사용할 수 없는 아이디입니다.';
		changeStyle($element, isSuccess, msg);
		validationChecks.userId = isSuccess;
	});
});

$("input[name=userPassword]").on("blur", function() { /*password blur*/
	let password = $(this).val().trim();
	if (!password) { return; }
	let isSuccess = validatePassword(password);
	const $ele = $(this).next("span");
	let msg = isSuccess ? '' : '8~16자의 영문 대/소문자, 숫자를 사용해 주세요.';
	changeStyle($ele, isSuccess, msg);
	validationChecks.userPassword = isSuccess;
	$("input[name=userPasswordCheck]").trigger("blur");
});

$("input[name=userPasswordCheck]").on("blur", function() { /*passwordCheck blur*/
	const $element = $(this).next("span");
	let passwordCheck = $(this).val().trim();
	let password = $("input[name=userPassword]").val().trim();
	if (!passwordCheck || !password) { return; }
	let isSuccess = password == passwordCheck;
	let msg = isSuccess ? '' : '비밀번호가 일치하지 않습니다.';
	changeStyle($element, isSuccess, msg);
	validationChecks.userPasswordCheck = isSuccess;
});




$("input[name=userEmail]").on("blur", function() { /*email blur*/
	const $ele = $(this).next("span");
	let email = $(this).val().trim();
	if (!email) { return; }
	let isSuccess = validateEmail(email);
	let msg = isSuccess ? '' : '이메일 주소가 정확한지 확인해 주세요.';
	changeStyle($ele, isSuccess, msg);
	validationChecks.userEmail = isSuccess;
});


$("input[name=userPhoneNumber]").on("blur", function() { /*phone blur*/
	const $ele = $(this).next("span");
	let phone = $(this).val().trim();
	if (!phone) { return; }
	let isSuccess = validatePhone(phone);
	let msg = isSuccess ? '' : '핸드폰번호가 정확한지 확인해 주세요.';
	changeStyle($ele, isSuccess, msg);
	validationChecks.userPhoneNumber = isSuccess;
});



$(".joinInputDiv > input[class=submit]").on("click", function(e) { /*submit*/
	e.preventDefault();
	if (!validateEmpty()) {
		return;
	}
	if (Object.values(validationChecks).includes(false)) {
		alert("모든 항목을 정확히 입력해주세요.");
		return;
	}
	$(this).closest("form").submit();
});


function changeStyle($element, isSuccess, msg) { /*$element = 변경할ele, isSuccess true면 green, false면 red, msg가 true면 show, false면 hide*/
	msg ? $element.show() : $element.hide();
	$element.text(msg);
	isSuccess ? $element.css("color", "#09aa5c") : $element.css("color", "red");
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


function validateEmpty() { /*input val empty check*/
	if (!$("input[name=userId]").val().trim()) {
		alert("아이디를 입력하세요");
		return false;
	}
	if (!$("input[name=userPassword]").val().trim()) {
		alert("비밀번호를 입력하세요");
		return false;
	}
	if (!$("input[name=userPasswordCheck]").val().trim()) {
		alert("비밀번호를 다시 입력해주세요");
		return false;
	}
	if (!$("input[name=userPhoneNumber]").val().trim()) {
		alert("전화번호를 입력하세요");
		return false;
	}
	if (!$("input[name=userEmail]").val().trim()) {
		alert("이메일을 입력하세요");
		return false;
	}
	return true;
}

