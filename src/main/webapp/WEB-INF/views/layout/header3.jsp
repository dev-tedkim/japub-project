<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="loginStatus" value="${empty sessionScope.userNum ? 'login' : 'logout'}" />

<header class="header">
  <h1>
    <img src="<c:url value='/static/images/logo/main-logo.jpg' />" width="" height="" />
  </h1>
  <nav>
    <ul>
      <li>
        <a class="menu-link" href="#">${loginStatus eq 'login' ? '로그인' : '내정보'}</a>
        <ul class="submenu">
          <li><a href='<c:url value="/${loginStatus}" />'>${loginStatus}</a></li>
          <c:choose>
          	<c:when test="${empty sessionScope.userNum}">
          		<li><a href="<c:url value='/join/term' />">회원가입</a></li>
          	</c:when>
          	<c:otherwise>
          		<li><a href="<c:url value='/mypage/check-password' />">마이페이지</a></li>
          	</c:otherwise>
          </c:choose>
          <li><a href="<c:url value='/find-account' />">계정찾기</a></li>
        </ul>
      </li>

      <li>
        <a class="menu-link" href="#">커뮤니티</a>
        <ul class="submenu">
          <li><a href="<c:url value='/main' />">메인페이지</a></li>
          <li><a target="_blank" href="https://blog.naver.com/japub">블로그</a></li>
          <li><a target="_blank" href="https://www.youtube.com/@joongangpub">유튜브</a></li>
        </ul>
      </li>

      <li>
        <a class="menu-link" href="#">게시판</a>
        <ul class="submenu">
          <li><a href="<c:url value='/board/list?category=free' />">자유게시판</a></li>
          <li><a href="<c:url value='/board/list?category=notice' />">공지사항</a></li>
          <li><a href="<c:url value='/board/list?category=upload' />">자료실</a></li>
        </ul>
      </li>

      <li>
        <a class="menu-link" href="#">회사정보</a>
        <ul class="submenu">
         <li><a href="<c:url value='/info' />">회사소개</a></li>
          <li><a href="<c:url value='/info/map' />">오시는길</a></li>
          <li><a href="<c:url value='/info/manu-script' />">원고모집</a></li>
          <li><a href="<c:url value='/info/store-info' />">전국판매서점</a></li>
          <li><a href="<c:url value='/info/exchange' />">교환/반품안내</a></li>
        </ul>
      </li>

      <li>
        <a href="<c:url value='/products/list' />">스토어</a>
        <ul class="submenu">
        </ul>
      </li>
      
      <c:if test="${sessionScope.isAdmin}">
	       <li>
	        <a href="#">관리자페이지</a>
	        <ul class="submenu">
	          <li><a href="<c:url value='/products/register' />">상품등록</a></li>
	        </ul>
	      </li>
      </c:if>
    </ul>
  </nav>
</header>
