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
						<li><a href="<c:url value='/user/term' />">회원가입</a></li>
					</c:if>
					<c:if test="${not empty sessionScope.userNum}">
						<li><a href="<c:url value='/user/checkPassword' />">마이페이지</a></li>
					</c:if>
				</ul></li>

			<li><a href="<c:url value='/main' />">메인페이지</a>
				<ul class="submenu">
					<li><a target="_blank" href="https://blog.naver.com/japub">블로그</a></li>
					<li><a target="_blank" href="https://www.youtube.com/@joongangpub">유튜브</a></li>
				</ul></li>

			<li><a href="#">게시판</a>
				<ul class="submenu">
					<li><a href="<c:url value='/board/list?category=free' />">문의게시판</a></li>
					<li><a href="<c:url value='/board/list?category=notice' />">공지사항</a></li>
					<li><a href="<c:url value='/board/list?category=upload' />">자료실</a></li>
				</ul></li>

			<li><a href="<c:url value='/info/info' />">회사소개</a>
				<ul class="submenu">
					<li><a href="<c:url value='/info/map' />">오시는길</a></li>
					<li><a href="<c:url value='/info/manuscript' />">원고모집</a></li>
					<li><a href="<c:url value='/info/store-info' />">전국판매서점</a></li>
					<li><a href="<c:url value='/info/exchange' />">교환/반품안내</a></li>
				</ul></li>
				
					<li><a href="#">스토어</a>
				<ul class="submenu">
					<li><a target="_blank" href="https://smartstore.naver.com/jaub?NaPm=ct%3Dm5t6jk0o%7Cci%3D0zK0000Zj8bBZU7yNv2P%7Ctr%3Dsa%7Chk%3Dc4483eb874487bbc01436c3ae48e8b79746c8d43%7Cnacn%3DjsBgBcglvKyI">네이버</a></li>
					<li><a target="_blank" href="https://shop.coupang.com/japub?locale=ko_KR&platform=p">쿠팡</a></li>
					<li><a target="_blank" href="https://minishop.gmarket.co.kr/japub">지마켓</a></li>
					<li><a target="_blank" href="https://stores.auction.co.kr/japub">옥션</a></li>
				</ul></li>
		</ul>
	</nav>
</header>