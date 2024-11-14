<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<c:set var="cl" value="${ pageContext.request.contextPath }" />
<c:set var="roomid" value="${ roomid }" />
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <title>${ chat.roomname }</title>
    <script>
        // 현재 로그인한 사용자 정보를 JavaScript로 전달
        const loggedInUsername = "${user.username}";
    </script>
</head>
<body>
    <header class="bg-gray-800 shadow-md">
        <div class="px-4 py-4 flex justify-between items-center">
            <a href="${ cl }/"><h1 class="text-xl font-bold text-white">채팅 애플리케이션</h1></a>
            <nav>
                <c:choose>
                    <c:when test="${ empty user }">
                        <ul class="flex space-x-6">
                            <li><a href="${ cl }/" class="text-white">홈</a></li>
                            <li><a href="${ cl }/signin" class="text-white">로그인</a></li>
                            <li><a href="${ cl }/signup" class="text-white">회원가입</a></li>
                        </ul>
                    </c:when>
                    <c:otherwise>
                        <ul class="flex space-x-6">
                            <li><a href="${ cl }/" class="text-white">홈</a></li>
                            <li><a href="${ cl }/profile/${ user.username }" class="text-white">${ user.username }님 프로필</a></li>
                            <form action="${ cl }/logout" method="post" autocomplete="off">
                                <button type="submit" class="text-white">로그아웃</button>
                            </form>
                            <li><a href="${ cl }/create" class="text-white">방 만들기</a></li>
                        </ul>
                    </c:otherwise>
                </c:choose>
            </nav>
        </div>
    </header>
    
    <div class="flex h-[calc(100vh-60px)] bg-gray-100">
        <div class="w-64 bg-gray-800 text-white p-6">
        	<h2 class="text-xl font-bold mt-4 mb-6">관리자 : ${ chat.ownername }</h2>
		  	<details class="mb-4">
		    	<summary class="cursor-pointer text-lg font-semibold text-gray-200 hover:text-blue-400">
		      		나
		    	</summary>
		    	<ul class="space-y-3">
		      		<li class="p-2 hover:bg-gray-700 rounded transition-colors duration-200">내가 그룹으로 있는 방</li>
		    		</ul>
		  	</details>
		
		 	<details class="mb-4">
			    <summary class="cursor-pointer text-lg font-semibold text-gray-200 hover:text-blue-400">
			      	채널
			    </summary>
			    <ul class="space-y-3">
			      	<li class="p-2 hover:bg-gray-700 rounded transition-colors duration-200">프로젝트</li>
			      	<li class="p-2 hover:bg-gray-700 rounded transition-colors duration-200">이슈</li>
			    </ul>
		  	</details>
		</div>
    
        <div class="flex-1 flex flex-col">
            <div class="bg-white border-b border-gray-300 p-4 flex items-center justify-between">
                <h2 class="text-lg font-semibold">${ chat.roomname } 채팅방</h2>
                <h2 class="text-lg font-semibold">방 고유 번호 : ${ chat.roomid }</h2>
            </div>
            
            <div class="flex-1 overflow-y-auto p-4 space-y-4 bg-white" id="message-container">
                <c:forEach var="message" items="${ messages }">
                    <div class="flex items-start space-x-3">
                        <div>
                            <p class="text-sm font-semibold">${ fn:escapeXml(message.username) }</p>
                            <p class="bg-gray-200 p-3 rounded-lg max-w-xs">${ fn:escapeXml(message.chattext) }</p>
                        </div>
                    </div>
                </c:forEach>
            </div>
    
            <div class="bg-gray-100 p-4 border-t border-gray-300">
                <!-- 사용자 이름 입력 모달은 이제 필요하지 않음 -->
                <div id="usernameModal" class="hidden absolute top-0 left-0 w-full h-full bg-black bg-opacity-50 flex justify-center items-center">
                    <div class="bg-white p-8 rounded shadow-lg">
                        <label for="usernameInput" class="block mb-2">사용자 이름을 입력하세요:</label>
                        <input type="text" id="usernameInput" class="p-2 border border-gray-300 rounded w-full mb-4" required />
                        <button type="button" onclick="enterChatRoom()" class="bg-blue-500 text-white p-2 rounded w-full">입장</button>
                    </div>
                </div>

                <!-- 메시지 전송 폼 -->
                <form onsubmit="sendMessage(event)" class="flex space-x-3">
				    <input type="text" id="messageInput" class="flex-1 p-2 border border-gray-300 rounded focus:outline-none focus:ring-2 focus:ring-blue-500" placeholder="메시지를 입력하세요..." required>
				    <button type="submit" class="bg-blue-500 text-white px-4 rounded hover:bg-blue-600 transition duration-200">전송</button>
				</form>
            </div>
        </div>
    </div>
        
    <jsp:include page="/WEB-INF/common/footer.jsp"></jsp:include>
</body>
</html>