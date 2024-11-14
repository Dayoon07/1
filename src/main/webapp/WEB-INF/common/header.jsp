<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<c:set var="cl" value="${ pageContext.request.contextPath }" />
	<header class="bg-gray-800 shadow-md">
        <div class="max-w-7xl mx-auto px-4 py-4 flex justify-between items-center">
            <a href="${ cl }/"><h1 class="text-xl font-bold text-white">채팅 애플리케이션</h1></a>
            <nav>
                <c:if test="${ empty user }">
                	<ul class="flex space-x-6">
						<li data-modal-target="authentication-modal" data-modal-toggle="authentication-modal" class="text-white hover:text-blue-500 cursor-pointer">
							검색
						</li>
						<div id="authentication-modal" tabindex="-1" aria-hidden="true" class="hidden overflow-y-auto overflow-x-hidden fixed top-0 right-0 left-0 z-50 justify-center items-start w-full md:inset-0 max-h-full">
	                        <div class="absolute p-4 w-full max-w-2xl" style="top: 0; transform: translateY(50%);">
	                            <div class="relative bg-white rounded-lg shadow dark:bg-gray-700">
	                                <div class="flex items-center justify-between p-4 md:p-5 border-b rounded-t dark:border-gray-600">
	                                    <h3 class="text-xl font-semibold text-gray-900 dark:text-white">검색 기능</h3>
	                                    <button type="button" data-modal-hide="authentication-modal"
	                                        class="end-2.5 text-gray-400 bg-transparent hover:bg-gray-200 hover:text-gray-900 rounded-lg text-2xl w-8 h-8 ms-auto inline-flex justify-center items-center dark:hover:bg-gray-600 dark:hover:text-white">
	                                        &times;
	                                    </button>
	                                </div>
	                                <div class="p-4">
	                                    <form action="${ cl }/searchChatroom/?=" method="get" autocomplete="off"
	                                        class="my-10 bg-white flex px-4 py-3 border-b border-[#333] focus-within:border-blue-500 overflow-hidden max-w-md mx-auto font-[sans-serif]">
	                                        <button type="submit">
	                                            <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 192.904 192.904" width="18px" class="fill-gray-600 mr-3">
	                                                <path d="m190.707 180.101-47.078-47.077c11.702-14.072 18.752-32.142 18.752-51.831C162.381 36.423 125.959 0 81.191 0 36.422 0 0 36.423 
	                                                    0 81.193c0 44.767 36.422 81.187 81.191 81.187 19.688 0 37.759-7.049 51.831-18.751l47.079 47.078a7.474 7.474 0 0 0 5.303 2.197 7.498 
	                                                    7.498 0 0 0 5.303-12.803zM15 81.193C15 44.694 44.693 15 81.191 15c36.497 0 66.189 29.694 66.189 66.193 0 36.496-29.692 66.187-66.189 
	                                                    66.187C44.693 147.38 15 117.689 15 81.193z">
	                                                </path>
	                                            </svg>
	                                        </button>
	                                        <input type="text" id="searchChatroom" name="searchChatroom" placeholder="채팅 방의 이름을 입력해주세요"
	                                            class="w-full outline-none text-lg" required>
	                                    </form>
	                                </div>
	                            </div>
	                        </div>
	                    </div>
	                    <li><a href="${ cl }/signin" class="text-white hover:text-blue-500">로그인</a></li>
	                    <li><a href="${ cl }/signup" class="text-white hover:text-blue-500">회원가입</a></li>
	                </ul>
                </c:if>
                <c:if test="${ not empty user }">
                	<ul class="flex space-x-6">
                		<li data-modal-target="authentication-modal" data-modal-toggle="authentication-modal" class="text-white hover:text-blue-500 cursor-pointer">
							검색
						</li>
						<div id="authentication-modal" tabindex="-1" aria-hidden="true" class="hidden overflow-y-auto overflow-x-hidden 
                        	fixed top-0 right-0 left-0 z-50 justify-center items-start w-full md:inset-0 max-h-full">
	                        <div class="absolute p-4 w-full max-w-2xl" style="top: 0; transform: translateY(50%);">
	                            <div class="relative bg-white rounded-lg shadow dark:bg-gray-700">
	                                <div class="flex items-center justify-between p-4 md:p-5 border-b rounded-t dark:border-gray-600">
	                                    <h3 class="text-xl font-semibold text-gray-900 dark:text-white">검색 기능</h3>
	                                    <button type="button" data-modal-hide="authentication-modal"
	                                        class="end-2.5 text-gray-400 bg-transparent hover:bg-gray-200 hover:text-gray-900 rounded-lg text-2xl w-8 h-8 ms-auto inline-flex justify-center items-center dark:hover:bg-gray-600 dark:hover:text-white">
	                                        &times;
	                                    </button>
	                                </div>
	                                <div class="p-4">
	                                    <form action="${ cl }/searchChatroom/?=" method="get" autocomplete="off"
	                                        class="my-10 bg-white flex px-4 py-3 border-b border-[#333] focus-within:border-blue-500 overflow-hidden max-w-md mx-auto font-[sans-serif]">
	                                        <button type="submit">
	                                            <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 192.904 192.904" style="width: 18px;" class="fill-gray-600 mr-3">
	                                                <path d="m190.707 180.101-47.078-47.077c11.702-14.072 18.752-32.142 18.752-51.831C162.381 36.423 125.959 0 81.191 0 36.422 0 0 36.423 
	                                                    0 81.193c0 44.767 36.422 81.187 81.191 81.187 19.688 0 37.759-7.049 51.831-18.751l47.079 47.078a7.474 7.474 0 0 0 5.303 2.197 7.498 
	                                                    7.498 0 0 0 5.303-12.803zM15 81.193C15 44.694 44.693 15 81.191 15c36.497 0 66.189 29.694 66.189 66.193 0 36.496-29.692 66.187-66.189 
	                                                    66.187C44.693 147.38 15 117.689 15 81.193z">
	                                                </path>
	                                            </svg>
	                                        </button>
	                                        <input type="text" id="searchChatroom" name="searchChatroom" placeholder="채팅 방의 이름을 입력해주세요"
	                                            class="w-full outline-none text-lg" required>
	                                    </form>
	                                </div>
	                            </div>
	                        </div>
	                    </div>
	                    <li><a href="${ cl }/profile/${ user.username }" class="text-white hover:text-blue-500">${ user.username }님 프로필</a></li>
	                    <form action="${ cl }/logout" method="post" autocomplete="off">
	                    	<button type="submit" class="text-white hover:text blue-500">로그아웃</button>
	                    </form>
	                    <li><a href="${ cl }/create" class="text-white hover:text-blue-500">방 만들기</a></li>
	                    <li><a href="${ cl }/myChatroom/${ user.userid }" class="text-white hover:text-blue-500">내가 만든 채팅방</a></li>
	                </ul>
                </c:if>
            </nav>
        </div>
    </header>