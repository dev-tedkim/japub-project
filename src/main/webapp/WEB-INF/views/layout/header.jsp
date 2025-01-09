<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="loginStatus"
	value="${empty sessionScope.userNum ? 'login' : 'logout'}" />
<header class="header">
<h1><img src="<c:url value='/static/image/header-logo.jpg' />" width="50" height="50"/></h1>
	<nav>
		<ul>
			<li><a href='<c:url value="/user/${loginStatus}" />'>${loginStatus}</a>
				<ul class="submenu">
					<c:if test="${empty sessionScope.userNum}">
						<li><a href="<c:url value='/user/join' />">회원가입</a></li>
					</c:if>
					<c:if test="${not empty sessionScope.userNum}">
						<li><a href="<c:url value='/user/checkPassword' />">마이페이지</a></li>
					</c:if>
				</ul></li>

			<li><a href="<c:url value='/main' />">메인페이지</a>
				<ul class="submenu">
					<li><a href="#">menu2-1</a></li>
					<li><a href="#">menu2-2</a></li>
				</ul></li>

			<li><a href="#">게시판</a>
				<ul class="submenu">
					<li><a href="<c:url value='/board/list?category=free' />">문의게시판</a></li>
					<li><a href="<c:url value='/board/list?category=notice' />">공지사항</a></li>
					<li><a href="<c:url value='/board/list?category=upload' />">자료실</a></li>
				</ul></li>

			<li><a href="#">회사소개</a>
				<ul class="submenu">
					<li><a href="<c:url value='/info/info' />">회사특징</a></li>
					<li><a href="<c:url value='/info/map' />">오시는길</a></li>
				</ul></li>
		</ul>
	</nav>
</header>