CREATE TABLE chatuser (
    userid NUMBER GENERATED ALWAYS AS IDENTITY (START WITH 1 INCREMENT BY 1 NOCACHE) PRIMARY KEY,
    username VARCHAR2(255 CHAR) NOT NULL UNIQUE, -- 유일 제약 조건 추가
    useremail VARCHAR2(255 CHAR) NOT NULL,
    userpassword VARCHAR2(255 CHAR) NOT NULL,
    userbio CLOB NOT NULL,
    userphone VARCHAR2(30) NOT NULL,
    date_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    status VARCHAR2(255 CHAR) DEFAULT 'offline' NOT NULL
);
CREATE TABLE chatroom (
    roomid NUMBER GENERATED ALWAYS AS IDENTITY (START WITH 1 INCREMENT BY 1 NOCACHE) PRIMARY KEY,
    roomname VARCHAR2(255 CHAR) NOT NULL,
    roomnameinname VARCHAR2(255 CHAR) NOT NULL,
    date_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL, -- 방 생성 일시
    ownername VARCHAR2(255 CHAR) REFERENCES chatuser(username) NOT NULL -- 방장 ID로 변경
);

CREATE TABLE chat_message (
    message_id NUMBER GENERATED ALWAYS AS IDENTITY (START WITH 1 INCREMENT BY 1 NOCACHE) PRIMARY KEY,
    roomid NUMBER REFERENCES chatroom(roomid) ON DELETE CASCADE,
    userid NUMBER REFERENCES chatuser(userid) ON DELETE CASCADE,
    chattext CLOB NOT NULL,
    date_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    username VARCHAR2(255) NOT NULL,
    type VARCHAR2(50),
    sender VARCHAR2(255),
    receiver VARCHAR2(255),
    data CLOB
);



2024-11-15 개선된 테이블
-- 사용자 테이블
CREATE TABLE chatuser (
    userid NUMBER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    username VARCHAR2(255 CHAR) NOT NULL UNIQUE,
    useremail VARCHAR2(255 CHAR) NOT NULL,
    userpassword VARCHAR2(255 CHAR) NOT NULL,
    userbio CLOB NOT NULL,
    userphone VARCHAR2(30) NOT NULL,
    date_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    status VARCHAR2(255 CHAR) DEFAULT 'offline' NOT NULL
);

-- 방 테이블
CREATE TABLE chatroom (
    roomid NUMBER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    roomname VARCHAR2(255 CHAR) NOT NULL,
    roomnameinname VARCHAR2(255 CHAR) NOT NULL,
    date_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    ownername VARCHAR2(255 CHAR) REFERENCES chatuser(username) NOT NULL,
    is_private BOOLEAN DEFAULT FALSE,  -- 비공개 여부
    room_description CLOB  -- 방 설명
);

-- 메시지 유형 테이블
CREATE TABLE message_type (
    type_id NUMBER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    type_name VARCHAR2(50) NOT NULL UNIQUE
);

-- 메시지 테이블
CREATE TABLE chat_message (
    message_id NUMBER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    roomid NUMBER REFERENCES chatroom(roomid) ON DELETE CASCADE,
    userid NUMBER REFERENCES chatuser(userid) ON DELETE CASCADE,
    chattext CLOB NOT NULL,
    metadata CLOB, -- 추가적인 메타 데이터를 저장할 필드
    date_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    sender VARCHAR2(255) REFERENCES chatuser(username),
    receiver VARCHAR2(255) REFERENCES chatuser(username),
    type VARCHAR2(50) REFERENCES message_type(type_name)
);

-- 인덱스 추가 (성능 향상을 위한 예시)
CREATE INDEX idx_chat_message_roomid ON chat_message(roomid);
CREATE INDEX idx_chat_message_userid ON chat_message(userid);
