package com.e.d.controller;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.e.d.model.entity.ChatMessageEntity;
import com.e.d.model.entity.ChatRoomEntity;
import com.e.d.model.entity.ChatUserEntity;
import com.e.d.model.repository.ChatMessageRepository;
import com.e.d.model.repository.ChatRoomRepository;
import com.e.d.model.repository.ChatUserRepository;
import com.e.d.model.service.ChatMessageService;
import com.e.d.model.service.ChatRoomService;
import com.e.d.model.service.ChatUserService;

import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class MainController {

	@Autowired
	private ChatRoomRepository roomRepository;
	
	@Autowired
	private ChatRoomService roomService;
	
	@Autowired
	private ChatUserRepository userRepository;
	
	@Autowired
	private ChatUserService userService;
	
	@Autowired
	private ChatMessageRepository messageRepository;
	
	@Autowired
	private ChatMessageService messageService;
	
	@GetMapping("/")
	public String index(
	        @RequestParam(defaultValue = "0") int page,
	        @RequestParam(defaultValue = "10") int size,
	        Model model) {
	    Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "roomid"));
	    Page<ChatRoomEntity> chatRoomPage = roomRepository.findAll(pageable);

	    StringBuilder pageBar = new StringBuilder();
	    pageBar.append("<nav aria-label=\"Page navigation example\">\n				<ul class=\"inline-flex -space-x-px text-lg my-10\">\n					");

	    // 이전 버튼
	    if (chatRoomPage.hasPrevious()) {
	        pageBar.append("<li><a href=\"/?page=" + (chatRoomPage.getNumber() - 1) + "\" class=\"flex "
	        		+ "items-center justify-center px-3 h-10 leading-tight text-gray-500 bg-white border border-gray-300 rounded-l-lg hover:bg-gray-100 hover:text-gray-700\">이전</a></li>");
	    }

	    // 페이지 범위 설정
	    int startPage = Math.max(0, page - 5);
	    int endPage = Math.min(chatRoomPage.getTotalPages(), page + 5);

	    for (int i = startPage; i < endPage; i++) {
	        if (i == chatRoomPage.getNumber()) {
	            pageBar.append("<li><a href=\"?page=0\" aria-current=\"page\" class=\"flex "
	            		+ "items-center justify-center px-5 h-10 text-blue-600 border border-gray-300 bg-blue-50 w-8\">" + (i + 1) + "</a></li>\n					");
	        } else {
	            pageBar.append("<li><a href=\"/?page=" + i + "\" class=\"flex "
	            		+ "items-center justify-center px-5 h-10 leading-tight text-gray-500 bg-white border border-gray-300 hover:bg-gray-100 hover:text-gray-700 w-8\">" + (i + 1) + "</a></li>\n					");
	        }
	    }

	    // 다음 버튼
	    if (chatRoomPage.hasNext()) {
	        pageBar.append("<li><a href=\"/?page=" + (chatRoomPage.getNumber() + 1) + "\" class=\"flex "
	        		+ "items-center justify-center px-3 h-10 leading-tight text-gray-500 bg-white border border-gray-300 rounded-r-lg hover:bg-gray-100 hover:text-gray-700\">다음</a></li>\n				");
	    }

	    pageBar.append("</ul>\n			</nav>");

	    model.addAttribute("AllChatRoom", chatRoomPage.getContent());
	    model.addAttribute("pageBar", pageBar.toString());
	    
//	    chatRoomPage.stream().forEach(n -> System.out.println(n));

	    return "index";
	}
	
	@GetMapping("/signin")
	public String signIn() {
		return "user/signin";
	}
	
	@GetMapping("/signup")
	public String signUp() {
		return "user/signup";
	}
	
	@PostMapping("/signup")
	public String signupForm(@ModelAttribute ChatUserEntity entity) {
	    try {
	        // dateTime이 설정되지 않았으면 현재 시간으로 설정
	        if (entity.getDateTime() == null) {
	            entity.setDateTime(LocalDateTime.now());
	        }

	        userRepository.save(entity);
	        return "redirect:/"; // 회원가입 성공 시 메인 페이지로 리다이렉트
	    } catch (Exception e) {
	        e.printStackTrace();
	        return "redirect:/signup"; // 오류 발생 시 회원가입 페이지로 리다이렉트
	    }
	}
	
	@PostMapping("/signin")
	public String signinForm(
	        @RequestParam String username,
	        @RequestParam String userpassword,
	        HttpSession session) {
	    // 사용자 이름으로 조회
	    List<ChatUserEntity> users = userRepository.findByUsername(username);
	    
	    // 사용자가 존재하고 비밀번호가 일치하는지 확인
	    if (!users.isEmpty() && users.get(0).getUserpassword().equals(userpassword)) {
	        ChatUserEntity user = users.get(0); // 첫 번째 사용자 선택
	        
	        user.setStatus("online");
	        userRepository.save(user);
	        
	        // 세션에 단일 사용자 객체와 상태 저장
	        session.setAttribute("user", user);
	        session.setAttribute("useStatus", "online");
	        
	        return "redirect:/";
	    }
	    
	    session.setAttribute("loginError", "Invalid username or password");
	    return "redirect:/signin";
	}
	
	@PostMapping("/logout")
	public String logout(HttpSession session) {
	    ChatUserEntity user = (ChatUserEntity) session.getAttribute("user");
	    
	    if (user != null) {
	        // 상태를 offline으로 변경하고 저장
	        user.setStatus("offline"); // 상태를 offline으로 변경
	        userRepository.save(user); // 상태 변경을 데이터베이스에 저장
	    }
	    
	    session.invalidate(); // 세션 무효화
	    return "redirect:/";
	}
	
	@GetMapping("/profile/{username}")
	public String getUserProfile(Model model, @PathVariable String username) {
	    List<ChatUserEntity> users = userRepository.findByUsername(username);
	    
	    if (!users.isEmpty()) {
	        ChatUserEntity userProfile = users.get(0);
	        model.addAttribute("userProfile", userProfile);
	        return "user/profile"; // 사용자 프로필 JSP로 이동
	    } else {
	        model.addAttribute("NoUserException", "사용자를 찾을 수 없습니다.");
	        return "error/NoUserException";
	    }
	}
	
	@PostMapping("/updateUserInfo")
	public String updateUserInfo(@RequestParam int userid,
	                              @RequestParam String username,
	                              @RequestParam String useremail,
	                              @RequestParam String userbio) {
	    try {
	        // 사용자의 기존 정보를 데이터베이스에서 조회
	        ChatUserEntity existingUser = userRepository.findById(userid)
	                .orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + userid));

	        // dateTime 필드를 기존 값으로 유지
	        LocalDateTime dateTime = existingUser.getDateTime();

	        // 기존 사용자 정보를 업데이트
	        existingUser.setUsername(username);
	        existingUser.setUseremail(useremail);
	        existingUser.setUserbio(userbio);
	        existingUser.setDateTime(dateTime); // dateTime은 유지

	        // 사용자 정보를 저장
	        userRepository.save(existingUser);

	        return "redirect:/";
	    } catch (Exception e) {
	        e.printStackTrace();
	        log.info("User update failed: {}", e.getMessage());
	        return "redirect:/";
	    }
	}
	
	@PostMapping("/userDelete/{userid}")
    public String userDelete(@PathVariable int userid, RedirectAttributes redirectAttributes) {
        try {
            userRepository.deleteById(userid);
            redirectAttributes.addFlashAttribute("message", "계정이 성공적으로 삭제되었습니다.");
            return "redirect:/"; // 삭제 후 메인 페이지로 리다이렉트
        } catch (Exception e) {
            log.error("계정 삭제 실패: userid={}, 오류={}", userid, e.getMessage());
            redirectAttributes.addFlashAttribute("error", "계정 삭제에 실패했습니다. 다시 시도해주세요.");
            return "redirect:/user/profile"; // 오류 발생 시 프로필 페이지로 리다이렉트
        }
    }
	
	@GetMapping("/create")
	public String create() {
		return "chat/create";
	}
	
	@PostMapping("/create/createChatRoom")
	public String createChatRoom(@ModelAttribute ChatRoomEntity entity) {
		roomRepository.save(entity);
		System.out.println(entity.getRoomname() + ", 시간 : " + entity.getDateTime());
		return "redirect:/";
	}
	
	@GetMapping("/chatroom/{roomid}")
	public String chatRoomMovement(HttpSession session, Model model, @PathVariable int roomid) {
	    // 세션에서 사용자 정보 가져오기
	    ChatUserEntity user = (ChatUserEntity) session.getAttribute("user");
	    
	    // 사용자 로그인 여부 확인
	    if (user == null) {
	        model.addAttribute("NotLoginUserAccess", "로그인을 하지 않으면 <br> 채팅을 이용할 수 없습니다.");
	        return "error/NotLoginUserAccess"; // 로그인하지 않은 경우 에러 페이지로 리다이렉트
	    }

	    // 주어진 roomid로 방 찾기
	    Optional<ChatRoomEntity> optionalRoom = roomRepository.findById(roomid);
	    
	    if (optionalRoom.isPresent()) {
	        ChatRoomEntity chatRoom = optionalRoom.get();
	        
	        // 해당 방의 메시지 목록을 messageRepository에서 가져오기
	        List<ChatMessageEntity> messages = messageRepository.findByRoomid(roomid);

	        // 모델에 방 정보와 메시지 추가
	        model.addAttribute("chat", chatRoom); // 채팅방 정보 추가
	        model.addAttribute("messages", messages); // 해당 방의 메시지 목록 추가
	        
	        return "chat/chat"; // 채팅 JSP 페이지로 이동
	    } else {
	        model.addAttribute("NullChatRoomIndex", "존재하지 않는 채팅방입니다.");
	        return "error/NullChatRoomIndex"; // 방이 존재하지 않는 경우 에러 페이지로 리다이렉트
	    }
	}
	
	@GetMapping("/chatroom/{roomid}/d")
	public ResponseEntity<List<ChatMessageEntity>> getChatRoomData(@PathVariable int roomid) {
	    List<ChatMessageEntity> messages = messageRepository.findByRoomid(roomid);
	    return ResponseEntity.ok(messages); // JSON 형식으로 메시지 반환
	}

	@GetMapping("/myChatroom/{userid}")
	public String myChatRoom(
	        @PathVariable int userid,
	        Model model) {
	    // 사용자 정보를 조회하여 ownername을 찾음
	    ChatUserEntity user = userRepository.findById(userid)
	            .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));
	    String ownername = user.getUsername();  // 또는 필요한 필드로 ownername을 가져옴

	    // 해당 사용자가 만든 채팅방만 조회
	    List<ChatRoomEntity> rooms = roomRepository.findByOwnername(ownername);
	    
	    // 모델에 채팅방 정보를 추가
	    model.addAttribute("rooms", rooms);

	    return "user/myChatroom";
	}


	/* @PostMapping("/chatroom/{roomid}/insertValue")
	public String insertChatMessage(
	        @PathVariable int roomid,
	        @RequestParam String chattext,
	        HttpSession session,
	        Model model) {

	    // 사용자 세션 확인
	    ChatUserEntity user = (ChatUserEntity) session.getAttribute("user");
	    if (user == null) {
	        return "redirect:/signin"; // 로그인하지 않은 경우 로그인 페이지로 리다이렉트
	    }

	    try {
	        // 메시지 엔티티 생성
	        ChatMessageEntity chatMessage = new ChatMessageEntity();
	        chatMessage.setRoomid(roomid); // 방 ID 설정
	        chatMessage.setUserid(user.getUserid()); // 현재 사용자 ID를 세팅
	        chatMessage.setChattext(chattext); // 입력된 채팅 메시지 설정
	        chatMessage.setDateTime(LocalDateTime.now()); // 현재 시간으로 설정
	        chatMessage.setUsername(user.getUsername());

	        messageRepository.save(chatMessage); // 메시지 저장

	        // 방의 모든 메시지 가져오기
	        List<ChatMessageEntity> messages = messageRepository.findByRoomid(roomid);
	        model.addAttribute("messages", messages); // 모델에 메시지 추가

	        // 채팅방 페이지로 이동
	        return "redirect:/chatroom/" + roomid; // JSP 파일 이름 (리다이렉트가 아닌 직접 반환)

	    } catch (Exception e) {
	        // 예외 처리: 로그 기록 후 에러 페이지로 이동
	        e.printStackTrace();
	        model.addAttribute("errorMessage", "메시지 저장 중 오류가 발생했습니다.");
	        return "error"; // 에러 JSP 파일
	    }
	} */
	
	
	
	
}
