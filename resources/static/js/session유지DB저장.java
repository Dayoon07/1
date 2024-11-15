@Component
public class ChatWebSocketHandler extends TextWebSocketHandler {

    private ObjectMapper mapper = new ObjectMapper();
    private Map<String, WebSocketSession> clients = new HashMap<>();
    
    private ChatMessageRepository chatMessageRepository;
    private ChatUserRepository chatUserRepository;
    
    public ChatWebSocketHandler(ChatMessageRepository chatMessageRepository, ChatUserRepository chatUserRepository) {
        this.chatMessageRepository = chatMessageRepository;
        this.chatUserRepository = chatUserRepository;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        System.out.println("클라이언트 접속! " + session.getId() + "가 접속함");
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        ChatMessageEntity msg = mapper.readValue(message.getPayload(), ChatMessageEntity.class);

        // 세션에서 사용자 이름 가져오기
        String username = (String) session.getAttributes().get("username");
        if (username == null) {
            // 사용자가 로그인하지 않은 경우 예외 처리
            session.close();
            return;
        }

        // 메시지에 사용자 이름 설정
        msg.setSender(username);

        switch (msg.getType()) {
            case "enter":
                addClient(session, msg);  // 클라이언트 입장 처리
                break;
            case "chat":
                saveMessageAndBroadcast(msg);  // 채팅 메시지 전송 및 DB 저장
                break;
            case "exit":
                removeClient(session, msg);  // 클라이언트 나가기 처리
                break;
        }
    }

    private void addClient(WebSocketSession session, ChatMessageEntity msg) {
        clients.put(msg.getSender(), session);
        msg.setData(msg.getSender() + "님이 입장하셨습니다.");
        broadcastSend(msg);  // 입장 메시지 전송
    }
    
    private void saveMessageAndBroadcast(ChatMessageEntity msg) {
        // DB에 메시지 저장
        Timestamp currentTime = new Timestamp(System.currentTimeMillis());
        msg.setDateTime(currentTime);  // createdAt 필드에 시간 저장

        // 메시지 본문에 추가할 내용을 작성
        msg.setData(msg.getSender() + ": " + msg.getData());

        // 메시지 DB에 저장
        chatMessageRepository.save(msg);

        // 클라이언트에게 메시지 전송
        broadcastSend(msg);
    }

    private void removeClient(WebSocketSession session, ChatMessageEntity msg) {
        String username = null;

        // 나간 사용자를 확인하고 해당 사용자 이름을 가져오기
        for (Map.Entry<String, WebSocketSession> entry : clients.entrySet()) {
            if (entry.getValue().equals(session)) {
                username = entry.getKey();
                clients.remove(entry.getKey());
                break;
            }
        }

        if (username != null) {
            // 사용자가 나갔다는 메시지를 생성
            msg.setType("chat");
            msg.setSender("System");
            msg.setData(username + " 님이 나갔습니다.");

            // 나간 사용자에 대한 메시지 브로드캐스트
            broadcastSend(msg);
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        removeClient(session, new ChatMessageEntity());  // 나간 사용자 처리
        super.afterConnectionClosed(session, status);
    }

    private void broadcastSend(ChatMessageEntity message) {
        for (WebSocketSession client : clients.values()) {
            try {
                TextMessage sendMsg = new TextMessage(mapper.writeValueAsString(message));
                client.sendMessage(sendMsg);  // 클라이언트에게 메시지 전송
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
