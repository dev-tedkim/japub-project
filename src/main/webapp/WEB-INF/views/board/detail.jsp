<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Insert title here</title>
    <link rel="preconnect" href="https://fonts.googleapis.com" />
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin />
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css" />
    <link href="https://fonts.googleapis.com/css2?family=Noto+Sans+KR&display=swap" rel="stylesheet" />
    <link rel="stylesheet" href="<c:url value='/static/css/style.css' />" />
    <link rel="stylesheet" href="<c:url value='/static/css/board-detail.css' />" />
    <link rel="stylesheet" href="<c:url value='/static/css/comment.css' />" />
</head>
<body>
    <jsp:include page="/WEB-INF/views/layout/header.jsp" />
    <main class="main">
        <div id="board-container" class="container" data-board-num="${board.boardNum}">
	            <div class="header-with-buttons">
	                <h1 class="title"><c:out value="${board.boardTitle}" /></h1>
	                <div class="btn-group">
	                    <a href="<c:url value='/board/list${criteria.params}&referrer=${criteria.referrer}' />" class="btn"><i class="fa fa-bars"></i> 목록</a>
	                    <c:if test="${sessionScope.userNum == board.userNum}">
	                    	<a href="<c:url value='/board/update${criteria.params}&boardNum=${board.boardNum}' />" class="btn"><i class="fa fa-pencil"></i> 수정</a>
	                    </c:if>
	                    <c:choose>
	                    	<c:when test="${sessionScope.userNum eq board.userNum and !sessionScope.isAdmin}">
	                    		<a href="<c:url value='/board/delete${criteria.params}&boardNum=${board.boardNum}' />" class="btn deleteBoardBtn"><i class="fa fa-trash"></i> 삭제</a>	
	                    	</c:when>
	                    	<c:when test="${sessionScope.isAdmin}">
	                    		<a href="<c:url value='/admin/board/delete${criteria.params}&boardNum=${board.boardNum}' />" class="btn deleteBoardBtn"><i class="fa fa-trash"></i> 삭제</a>
	                    	</c:when>  
	                    </c:choose>
	                </div>
	            </div>
            <div class="post-info">
                <span class="post-info-span"><i class="fa fa-user-circle"></i><c:out value="${board.userId}" /></span>
                <span class="post-info-span"><i class="fa fa-calendar"></i><c:out value="${board.boardRegisterDate}" /></span>
                <span class="post-info-span">조회수: <c:out value="${board.boardReadCount}" /></span>
                <span class="post-info-span">댓글: <c:out value="${board.boardCommentCount}" /></span>
            </div>
            <article class="post-content">
                <div class="board-content">
                    <c:out value="${board.boardContent}" />
                </div>
                <div class="board-content-line"></div>
            </article>

            <div class="thumbnailDiv">
                <ul class="thumbnailUl"></ul>
            </div>
        </div>

        <div class="comment-container">
            <ul class="comment-ul"></ul>
            <div class="comment-input">
                <div>
                    <span class="writer">${userId}</span>
                </div>
                <textarea rows="5" cols="30" placeholder="댓글을 입력해보세요."></textarea>
                <div>
                    <a href="#" data-board-num="${board.boardNum}" class="comment-insert-btn">등록</a>
                </div>
            </div>
        </div>
    </main>

    <jsp:include page="/WEB-INF/views/layout/footer.jsp" />

    <li class="update-li">
        <span class="profile">
            <i class="fa fa-user-circle" aria-hidden="true"></i>
        </span>
        <section>
            <div>
                <span class="writer">${userId}</span>
            </div>
            <textarea cols="30" rows="5" placeholder="댓글을 입력해보세요."></textarea>
            <div class="update-li-btn-div">
                <a href="#" class="comment-cancel-btn">취소</a>
                <a href="#" class="comment-update-done">등록</a>
                <a href="#" class="comment-reply-done" data-reply="reply">등록</a>
            </div>
        </section>
    </li>
</body>

<script src="https://code.jquery.com/jquery-3.7.1.js"></script>
<script>
    const msg = '${msg}';
    if (msg) { alert(msg); }
    const contextPath = '${pageContext.request.contextPath}';
    const sessionUserNum = '${sessionScope.userNum}';
    const category = "upload";
    const currentCategory = '${criteria.category}';
    let strIsAdmin = '${sessionScope.isAdmin}';
    const isAdmin = JSON.parse(strIsAdmin);
    
</script>
<script src="<c:url value='/static/js/script.js' />"></script>
<script src="<c:url value='/static/js/file.js' />"></script>
<script src="<c:url value='/static/js/board.js' />"></script>
<script src="<c:url value='/static/js/comment.js' />"></script>
<script>
    if (category === currentCategory) {
        showThumbnails($thumbnailUl, boardNum, true);
    }
</script>
</html>
