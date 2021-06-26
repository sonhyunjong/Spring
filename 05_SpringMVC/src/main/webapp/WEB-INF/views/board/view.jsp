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
<script src="${ path }/js/jquery-3.6.0.min.js"></script>
</head>
<body>
<h2>게시판</h2>
<div id="board-write-container">
	<table id="tbl-board">
		<tr>
			<th>글번호</th>
			<td>${board.no}</td>
		</tr>
		<tr>
			<th>제 목</th>
			<td>${board.title}</td>
		</tr>
		<tr>
			<th>작성자</th>
			<td>${board.writerId}</td>
		</tr>
		<tr>
			<th>조회수</th>
			<td>${board.readCount}</td>
		</tr>
		<tr>
			<th>첨부파일</th>
			<td>
				<c:if test="${ !empty board.originalFileName }">
					<a href="javascript:fileDownload('${ board.originalFileName }', '${ board.renamedFileName }')">
						<img src="${ path }/images/file.png" width="20" height="20"/>
						<c:out value="${ board.originalFileName }"></c:out>						
					</a>
					<script>
						function fileDownload(oriname, rename) {
							const url = "${ path }/board/fileDown";
							
							let oName = encodeURIComponent(oriname);
							let rName = encodeURIComponent(rename);
							
							location.assign(url + "?oriname=" + oName + "&rename=" + rName);
						}
					</script>	
				</c:if>
				<c:if test="${ empty board.originalFileName }">
					<span> - </span>
				</c:if>
			</td>
		</tr>
		<tr>
			<th>내 용</th>
			<td>${ board.content }</td>
		</tr>
		<%--글작성자/관리자인경우 수정삭제 가능 --%>
		<tr>
			<th colspan="2">
				<c:if test="${ !empty loginMember && (loginMember.id == board.writerId 
									|| loginMember.role == 'ROLE_ADMIN') }">
					<button type="button" id="btnUpdate">수정</button>
					<button type="button" id="btnDelete">삭제</button>
				</c:if>

				<button type="button" onclick="location.href='${ path }/board/list'">목록으로</button>
			</th>
		</tr>
	</table>
</div>
   
<script>
	$(document).ready(() => {
		$("#btnUpdate").on("click", (e) => {
			location.href = "${path}/board/update?no=${board.no}";
		});
		
		$("#btnDelete").on("click", (e) => {
			if(confirm("정말로 게시글을 삭제 하시겠습니까?")) {
				location.replace("${path}/board/delete?boardNo=${board.no}");
			}
		});
	});
</script>
</body>
</html>