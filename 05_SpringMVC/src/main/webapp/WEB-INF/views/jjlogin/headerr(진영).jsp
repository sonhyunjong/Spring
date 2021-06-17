<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
      
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<c:set var="path" value="${pageContext.request.contextPath}"/>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>

<!-- 아이콘 라이브러리 link -->
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/font-awesome/4.7.0/css/font-awesome.min.css">

<!-- 제이쿼리 import -->
<script src="${path}/js/jquery-3.6.0.min.js"></script>

<style>
	*{margin:0; padding:0;}
	.headerArea, .introArea{
		width:1200px;
		height:82px;
		border:1px solid gray;
		margin:0 auto;
	}
	ul.headerMenu, ul.introMenu {
		list-style-type:none;
	}
	ul.headerMenu li {
		width:150px;
		height:80px;
		border:1px solid gray;
		float:left;
		font-size:1.4em;
		font-weight:bold;
		text-align:center;
		line-height:3.5;
		margin-left:60px;
	}
	ul.headerMenu li:nth-child(6){
		width:50px;
		height:80px;
	}
	ul.introMenu li {
		width:120px;
		height:50px;
		border:1px solid gray;
		float:left;
		font-size:1em;
		font-weight:bold;
		text-align:center;
		line-height:3;
		display:none;
	}
	ul.introMenu li:nth-child(1){
		margin-left:10px;
	}
	#hamburger{
		width:500px;
		height:800px;
		border:1px solid gray;
		position:relative;
		background-color:#e5ed47;
		left:700px;
		top:-1px;
		
	}
</style>
		<script>
			$(document).ready(()=>{
				$(".introduce").on("mouseenter", () => {
					$(".introMenu li").slideDown(200);
				});
				$(".introduce").on("mouseleave", () => {
					$(".introMenu li").slideUp(200);
				});
				$("#hamburger").hide();
				$(".btnHBG").on("click", () => {
					$("#hamburger").animate({width:'toggle'});
				});
			});
			
		</script>
</head>
<body>
	<header>
		<div class="headerArea">
			<nav>
				<ul class="headerMenu">
					<li class="introduce"><a href="#">소개</a></li>
					<li><a href="#">챌린지</a></li>
					<li>
						<a href="#">
							<!-- 로고 이미지 -->
							<img src="${path}/resources/img/mung.jpg" width="150px" height="82px"/>
						</a>
					</li>
					<li><a href="#">고객센터</a></li>
					<li><a href="#">후기게시판</a></li>
					<li class="btnHBG"><a href="#"><i class="fa fa-bars" aria-hidden="true"></i></a></li>
				</ul>
			</nav>
			<div class="introArea">
				<nav>
					<ul class="introMenu">
						<li><a href="#">홈페이지 소개</a></li>
						<li><a href="#">개발자 소개</a></li>
					</ul>
				</nav>
			</div>
			<div id="hamburger">
				<sapn onclick="this.parentElement.style.display='none'"class">닫기</sapn>
			</div>
		</div>
	</header>
</body>
</html>