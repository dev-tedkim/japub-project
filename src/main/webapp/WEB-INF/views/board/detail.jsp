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
			        <span>상세보기</span>
			        <div class="buttonBox">
			          <a href="<c:url value='/board/list${criteria.params}' />"><i class="fa fa-bars"></i> 목록</a>
			          <c:if test="${board.userNum eq sessionScope.userNum}">
				          <a href="<c:url value='/board/update${criteria.params}&boardNum=${board.boardNum}' />"><i class="fa fa-pencil"></i> 수정</a>
				          <a class="deleteBoardBtn" href="<c:url value='/board/delete${criteria.params}&boardNum=${board.boardNum}' />"><i class="fa fa-trash"></i> 삭제</a>
			          </c:if> 
			        </div>
			      </section>
			      <div class="board-info">
			        <ul>
			          <li>
			            <span class="icon">
			              <i class="fa fa-user-circle" aria-hidden="true"></i>
			            </span>
			            <span class="writer">${board.userId}</span>
			          </li>
			          <li>조회&nbsp;:&nbsp;<span>${board.boardHit}</span></li>
			          <li>댓글&nbsp;:&nbsp;<span>${commentCount}</span></li>
			          <li>
			            <span class="icon">
			              <i class="fa fa-calendar"></i>
			            </span>
			            <span>${board.boardRegisterDate}</span>
			          </li>
			        </ul>
			      </div>
			      <form name="registerForm" method="post" autocomplete="off">
			        <input type="hidden" name="boardNum" value="${board.boardNum}" readonly="readonly"/>
			        <input type="text" name="boardTitle" placeholder="제목을 입력하세요" value="${board.boardTitle}" readonly="readonly"/>
			        <textarea name="boardContent" rows="20" placeholder="내용을 입력하세요" readonly="readonly"><c:out value="${board.boardContent}" /></textarea>  
			      </form>
			      <div class="thumbnailDiv">
			      		<ul class="thumbnailUl"></ul>
			      </div>
    		</div>
    		
    	<div class="commentContainer">
     	 <ul class="commentUl"></ul>

      <div class="showCommentBtn">
        <a href="showMoreCommentBtn">댓글 더보기</a>
      </div>

      <div class="commentInput">
        <div>
          <span class="writer">${empty userId ? '' : userId}</span>
        </div>
        <textarea rows="5" cols="30" placeholder="댓글을 입력해보세요."></textarea>
        <div>
          <a href="#" class="writeCommentBtn">등록</a>
        </div>
      </div>
    </div>
	    </main>
	<jsp:include page="/WEB-INF/views/layout/footer.jsp"/>
	<div class="updateDiv">
		<li class="updateLi">
          <span class="profile">
            <i class="fa fa-user-circle" aria-hidden="true"></i>
          </span>
          <section>
            <div>
              <span class="writer">${userId}</span>
            </div>
            <textarea cols="30" rows="5" placeholder="댓글을 입력해보세요."></textarea>
            <div class="updateLiBtnDiv">
              <a href="#" class="cancelCommentBtn">취소</a>
              <a href="#" class="updateSubmitBtn">등록</a>
              <a href="#" class="replySubmitBtn">등록</a>
            </div>
          </section>
        </li>
	</div>
</body>
<script src="https://code.jquery.com/jquery-3.7.1.js"></script>
<script>
	const $updateLi = $("li.updateLi");
	let msg = '${msg}';
	if(msg){alert(msg);}
	let contextPath = '${pageContext.request.contextPath}';
	let sessionUserNum = '${sessionScope.userNum}';
	let boardNum = '${board.boardNum}';
	let page = 1;
	let flag = false;
</script>
<script src="<c:url value='/static/js/script.js' />"></script>
<script src="<c:url value='/static/js/comment.js' />"></script>
<script src="<c:url value='/static/js/file.js' />"></script>
<script>commentService.showComments();</script>
<script>fileService.getFiles(true);</script>
</html>