<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<c:set var="cl" value="${ pageContext.request.contextPath }" />
<!DOCTYPE html>
<html lang="ko">
<head>
	<meta charset="UTF-8">
	<title>${ NullChatRoomIndex }</title>
</head>
<body>
	<jsp:include page="/WEB-INF/common/header.jsp"></jsp:include>
	
	<h1 class="text-center text-black text-3xl mt-5">${ NullChatRoomIndex }</h1><br><br>
	<div class="w-40 h-12 max-w-md mx-auto">
		<a href="${ cl }/">
			<button class="relative inline-flex items-center justify-center p-0.5 mb-2 me-2 overflow-hidden text-2xl font-medium text-gray-900 rounded-lg group bg-gradient-to-br from-green-400 to-blue-600 group-hover:from-green-400 group-hover:to-blue-600 hover:text-white dark:text-white focus:ring-4 focus:outline-none focus:ring-green-200 dark:focus:ring-green-800">
				<span class="relative px-5 py-2.5 transition-all ease-in duration-75 bg-white dark:bg-gray-900 rounded-md group-hover:bg-opacity-0">
					돌아가기
				</span>
			</button>
		</a>
	</div>
	
	<jsp:include page="/WEB-INF/common/footer.jsp"></jsp:include>
</body>
</html>