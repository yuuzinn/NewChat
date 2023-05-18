# NewChat 🌐
누구나 소통할 수 있는 채팅 서비스입니다.

# 프로젝트 기능 및 설계 

## USER
1. 회원가입 : Client가 해당 서비스를 이용하기 위해 계정이 없다면 회원가입을 합니다.
2. 로그인 : 사용자의 계정으로 로그인을 합니다.
3. 로그아웃 : 사용자가 현재 로그인 중인 계정을 로그아웃 합니다.

## Chat

1. 채팅방 생성 <br>사용자끼리의 채팅할 공간이 있어야 합니다. 이때, 사용자는 채팅방을 생성할 수 있습니다.
2. 채팅방 목록 조회 
<br> 2-1) 현재 채팅방의 목록을 조회할 수 있어야 합니다.
<br> 2-2) 사용자(자신)가 만든 채팅방 목록을 조회할 수 있어야 합니다.
<br>2-3) 사용자(자신)가 들어간(가입) 채팅방 목록을 조회할 수 있어야 합니다.
3. 채팅 조회
<br>이전에 했던 채팅들을 조회할 수 있어야 합니다.
4. 채팅방 입장
<br> 채팅방의 key를 통해 입장할 수 있습니다.
5. 채팅방 나가기
<br> 5-1) 구성원만 채팅방을 나갈 수 있습니다.
<br> 5-2) 사용자(자신)가 들어간(가입) 채팅방을 나갈 수 있습니다.
6. 채팅방 삭제
<br>채팅방을 생성한 사용자만 삭제할 수 있습니다.

# ERD ver 1.0 
![image](https://user-images.githubusercontent.com/119172260/236689573-141d01e8-7992-4827-8721-f2ca09adb72e.png)
# Architecture ver 1.0


![version1 drawio (1)](https://user-images.githubusercontent.com/119172260/235178812-62b8b029-537b-4670-a387-cb2bd3b7432c.png)


# Stack ⚒️
- Java 11
- Spring boot
- Gradle
- MySQL
- Web Socket
- Redis

# API 💻

[My API ver 1.0](https://www.notion.so/NewChat-147b4c7ceb5f48d0911f4b7af08dbd66?pvs=4#d2e4e87a42504e389faedd257e1aac15)

# ERD 🖇️

[My ERD ver 1.0](https://github.com/yeb0/NewChat/wiki/Architecture,-ERD-ver-1.0)

# WIKI 📜
[My WIKI](https://github.com/yeb0/NewChat/wiki)

