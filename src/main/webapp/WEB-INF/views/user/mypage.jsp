<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<link rel="preconnect" href="https://fonts.googleapis.com" />
<link rel="preconnect" href="https://fonts.gstatic.com" crossorigin />
<link
	href="https://fonts.googleapis.com/css2?family=Noto+Sans+KR&display=swap"
	rel="stylesheet" />
<link rel="stylesheet" href="<c:url value='/static/css/style.css' />" />
</head>
<body>
	<jsp:include page="/WEB-INF/views/layout/header.jsp"/>
		<main class="main">
		    	<div class="joinBox">
				      <form:form name="joinForm" autocomplete="off" modelAttribute="userDto" method="post" class="mypageForm">
					        <div class="joinHeader">
					          <span class="title">마이페이지</span>
					        </div>
					        <div class="joinInputDiv">
					          <input type="text" name="userId" placeholder="아이디" autofocus value="${user.userId}" readonly="readonly"/>
					          <form:errors path="userId" cssClass="errorMsg"/>
					        </div>
					        <div class="joinInputDiv">
					          <input type="password" name="userPassword" placeholder="비밀번호" value="${user.userPassword}" readonly="readonly" autoComplete="off" />
							  <form:errors path="userPassword" cssClass="errorMsg"/>
							  <span class="errorMsg"></span>
					        </div>
					         <div class="joinInputDiv">
					          <input type="password" name="userPasswordCheck" class="userPasswordCheck" placeholder="비밀번호 재입력" autoComplete="off" />
					          <span class="errorMsg"></span>
					        </div>
					        <div class="joinInputDiv">
					          <input type="text" name="userPhoneNumber" placeholder="핸드폰" value="${user.userPhoneNumber}" readonly="readonly"/>
					          <form:errors path="userPhoneNumber" cssClass="errorMsg"/>
					        </div>
					        <div class="joinInputDiv">
					          <input type="text" name="userEmail" placeholder="이메일" value="${user.userEmail}" readonly="readonly"/>
					          <form:errors path="userEmail" cssClass="errorMsg"/>
					        </div>
					        <div class="findAddress">
					          <div>
					            <input type="text" id="sample6_postcode" name="userZipCode" placeholder="우편번호" value="${user.userZipCode}" readonly="readonly"/>
					            <input type="button" onclick="" value="검색" />
					          </div>
					          <form:errors path="userZipCode" cssClass="errorMsg"/>
					        </div>
					        <div class="joinInputDiv">
					          <input type="text" id="sample6_address" name="userAddress" placeholder="주소" value="${user.userAddress}"  readonly="readonly"/>
					          <form:errors path="userAddress" cssClass="errorMsg"/>
					        </div>
					        <div class="joinInputDiv">
					          <input
					            type="text"
					            id="sample6_detailAddress"
					            name="userDetailAddress"
					            placeholder="상세주소"
					            value="${user.userDetailAddress}"
					            readonly="readonly"
					          />
					        </div>
					        <div class="joinInputDiv">
					          <input type="button" class="cancelChangePwBtn" value="취소" />
					        </div>
					        <div class="joinInputDiv">
					          <input type="button" class="changePwBtn" value="수정하기" />
					          <input type="submit" class="changePwOkbtn" value="변경완료" />
					        </div>
				      </form:form>
	    		</div>
		</main>
	<jsp:include page="/WEB-INF/views/layout/footer.jsp"/>
</body>
<script src="https://code.jquery.com/jquery-3.7.1.js"></script>
<script>
	let msg = '${msg}';
	if(msg){alert(msg);}
	let contextPath = '${pageContext.request.contextPath}';
	let sessionUserNum = '${sessionScope.userNum}';
	const $mypageForm = $("form[class=mypageForm]");
	const $pwInput = $mypageForm.find("input[name=userPassword]");
	const $pwCheckInput = $mypageForm.find("input[class=userPasswordCheck]");
	let userPassword = "";
</script>
<script src="<c:url value='/static/js/script.js' />"></script>
<script src="//t1.daumcdn.net/mapjsapi/bundle/postcode/prod/postcode.v2.js"></script>
<script src="<c:url value='/static/js/join.js' />"></script>
<script>
console.log($("input[name=userId]").prop("readonly"));
$("input.changePwOkbtn").on("click", function(e) { /*mypage 수정완료 클릭*/
	e.preventDefault();
	if (!validateEmpty()) { return; }
	const { userPassword, userPasswordCheck } = validationChecks;
	if ([userPassword, userPasswordCheck].includes(false)) {
		alert("모든 항목을 정확히 입력해주세요.");
		return;
	}
	$(this).closest("form[name=joinForm]").submit();
});
</script>
	
	
</html>