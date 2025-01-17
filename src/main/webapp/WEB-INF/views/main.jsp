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
<link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/swiper@11/swiper-bundle.min.css" />
</head>
<body>
	<jsp:include page="/WEB-INF/views/layout/header.jsp"/>
	 <main class="main">
      <section class="slide-section">
	      <div class="slide-container">
		        <div id="slide-swiper" class="swiper">
		          <!-- Additional required wrapper -->
		          <div class="swiper-wrapper">
		            <!-- Slides -->
		            <div class="swiper-slide">
		              <img src="<c:url value='/static/image/slide/slide1.jpg' />" />
		            </div>
		            <div class="swiper-slide">
		               <img src="<c:url value='/static/image/slide/slide2.jpg' />" />
		            </div>
		          </div>
		          <!-- If we need pagination -->
		          <div class="swiper-pagination"></div>
	        	</div>
	        </div>
      </section>
        <section class="book-section">
      <div class="book-container">
        <div class="book-title">
          <h1>신간도서</h1>
          <div class="book-title-right">
            <a href="#">더보기 +</a>
          </div>
        </div>
        <!-- Swiper -->
        <div id="book-swiper" class="swiper">
          <div class="swiper-wrapper book-wrapper">
            <div class="swiper-slide"><img src="<c:url value='/static/image/new/1.jpg' />" width="300px" height="auto" /></div>
            <div class="swiper-slide"><img src="<c:url value='/static/image/new/2.jpg' />"  /></div>
            <div class="swiper-slide"><img src="<c:url value='/static/image/new/3.jpg' />"  /></div>
            <div class="swiper-slide"><img src="<c:url value='/static/image/new/4.jpg' />" /></div>
            <div class="swiper-slide"><img src="<c:url value='/static/image/new/5.jpg' />" /></div>
              <div class="swiper-slide"><img src="<c:url value='/static/image/new/2.jpg' />" /></div>
            <div class="swiper-slide"><img src="<c:url value='/static/image/new/3.jpg' />" /></div>
            <div class="swiper-slide"><img src="<c:url value='/static/image/new/4.jpg' />" /></div>
          </div>

          <button
            type="button"
            class="swiper-button-prev type_white swiper-button-disabled"
            tabindex="0"
            role="button"
            aria-label="Previous slide"
            aria-disabled="true"
          ></button>
          <button
            type="button"
            class="swiper-button-next type_white"
            tabindex="0"
            role="button"
            aria-label="Next slide"
            aria-disabled="false"
          ></button>
        </div>
      </div>
    </section>
    
      <section class="book-section">
      <div class="book-container">
        <div class="book-title">
          <h1>추천도서</h1>
          <div class="book-title-right">
            <a href="#">더보기 +</a>
          </div>
        </div>
        <!-- Swiper -->
        <div id="book-swiper" class="swiper">
          <div class="swiper-wrapper book-wrapper">
            <div class="swiper-slide"><img src="<c:url value='/static/image/recomend/1.jpg' />" width="300px" height="auto" /></div>
            <div class="swiper-slide"><img src="<c:url value='/static/image/recomend/2.jpg' />"  /></div>
            <div class="swiper-slide"><img src="<c:url value='/static/image/recomend/3.jpg' />"  /></div>
            <div class="swiper-slide"><img src="<c:url value='/static/image/recomend/4.jpg' />" /></div>
            <div class="swiper-slide"><img src="<c:url value='/static/image/recomend/5.jpg' />" /></div>
              <div class="swiper-slide"><img src="<c:url value='/static/image/recomend/1.jpg' />" /></div>
            <div class="swiper-slide"><img src="<c:url value='/static/image/recomend/2.jpg' />" /></div>
            <div class="swiper-slide"><img src="<c:url value='/static/image/recomend/3.jpg' />" /></div>
          </div>

          <button
            type="button"
            class="swiper-button-prev type_white swiper-button-disabled"
            tabindex="0"
            role="button"
            aria-label="Previous slide"
            aria-disabled="true"
          ></button>
          <button
            type="button"
            class="swiper-button-next type_white"
            tabindex="0"
            role="button"
            aria-label="Next slide"
            aria-disabled="false"
          ></button>
        </div>
      </div>
    </section>
      <section class="layout-section">
      	  <div class="layout-container">
	        <div class="layout-box">
	          <div class="video">
	            <div class="video-header">
	              <h2>중앙 TV</h2>
	              <a
	                href="https://www.youtube.com/@joongangpub"
	                target="_blank"
	                class="video-btn notice-btn"
	                >+</a
	              >
	            </div>
	            <div class="video-content">
	              <iframe
	                src="https://www.youtube.com/embed/zdD80ti_JPI?si=uTqSVYl9gTll0CcZ&loop=1&mute=1&autoplay=1"
	                title="YouTube video player"
	                frameborder="0"
	                allow="accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture; web-share"
	                referrerpolicy="strict-origin-when-cross-origin"
	                allowfullscreen
	              ></iframe>
	            </div>
	          </div>
	        </div>
	        <div class="layout-box">
	          <div class="notice">
	            <div class="notice-header">
	              <h2>공지사항</h2>
	              <a href="<c:url value='/board/list?category=notice' />" class="notice-btn">+</a>
	            </div>
	            <div class="notice-body">
	              <ul>
	              <c:forEach items="${noticeBoards}" var="notice" begin="0" end="4">
		                <li>
		                  <a href="<c:url value='/board/detail?${noticeCriteria.params}&boardNum=${notice.boardNum}' />">
		                    <div class="notice-content">${notice.boardTitle}</div>
		                  </a>
		                  <div class="notice-date">${notice.boardRegisterDate}</div>
		                </li>
	               </c:forEach>
	              </ul>
	            </div>
	          </div>
	        </div>
	        <div class="layout-box">
	          <div class="notice">
	            <div class="notice-header">
	              <h2>자료실</h2>
	              <a href="<c:url value='/board/list?category=upload' />" class="notice-btn">+</a>
	            </div>
	            <div class="notice-body">
	              <ul>
	               <c:forEach items="${uploadBoards}" var="upload" begin="0" end="4">
		                <li>
		                  <a href="<c:url value='/board/detail?${uploadCriteria.params}&boardNum=${upload.boardNum}' />">
		                    <div class="notice-content">${upload.boardTitle}</div>
		                  </a>
		                  <div class="notice-date">${upload.boardRegisterDate}</div>
		                </li>
	                </c:forEach>
	              </ul>
	            </div>
	          </div>
	        </div>
	      </div>  
      </section>
    </main>
	<jsp:include page="/WEB-INF/views/layout/footer.jsp"/>
</body>
<script src="https://cdn.jsdelivr.net/npm/swiper@11/swiper-bundle.min.js"></script>
<script src="<c:url value='/static/js/main.js' />"></script>
<script src="https://code.jquery.com/jquery-3.7.1.js"></script>
<script>
	let msg = '${msg}';
	if(msg){alert(msg);}
	let contextPath = '${pageContext.request.contextPath}';
	let sessionUserNum = '${sessionScope.userNum}';
</script>
<script src="<c:url value='/static/js/script.js' />"></script>



</html>