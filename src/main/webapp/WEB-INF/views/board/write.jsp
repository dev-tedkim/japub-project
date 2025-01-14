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
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css"/>
<link
	href="https://fonts.googleapis.com/css2?family=Noto+Sans+KR&display=swap"
	rel="stylesheet" />
<link rel="stylesheet" href="<c:url value='/static/css/style.css' />" />
</head>
<body>
	<jsp:include page="/WEB-INF/views/layout/header.jsp"/>
	<main class="main">
	    <div class="writeContainer">
	      <section>
	        <span>글쓰기</span>
	        <div class="buttonBox">
	          <a href="<c:url value='/board/list${criteria.params}' />"><i class="fa fa-bars"></i> 취소</a>
	          <a class="writeBoardBtn" data-formname="writeForm"><i class="fa fa-pencil"></i> 등록</a>
	        </div>
	      </section>
	      <form name="registerForm" class="writeForm" method="post" autocomplete="off">
	        <input type="hidden" name="boardNum" value="" />
	        <input type="text" name="boardTitle" placeholder="제목을 입력하세요" value="${board.boardTitle}" />
	        <textarea name="boardContent" rows="20" placeholder="내용을 입력하세요"><c:out value="${board.boardContent}" /></textarea>
	        <c:if test="${not empty adminNum}">
		       <input type="file" name="multipartFiles" multiple />
		       <div class="thumbnailDiv">
			   		<ul class="thumbnailUl"></ul>
			   </div>  
			</c:if>
	      </form>
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
</script>
<script src="<c:url value='/static/js/script.js' />"></script>
<script src="<c:url value='/static/js/file.js' />"></script>
</html>