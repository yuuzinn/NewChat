# NewChat 🌐
누구나 소통할 수 있는 채팅 서비스입니다.

# 프로젝트 기능 및 설계 
...2.0으로 넘어가기 전, 몇 가지 기능이 추가되었습니다.
## **User**

- 회원가입
    - 같은 아이디의 회원가입은 불가능합니다.
- 로그인
    - 로그인을 하지 않으면 서비스 이용이 불가합니다.
- 로그아웃
    - 사용 중인 계정 로그아웃합니다.
- 회원수정
    - 로그인한 회원(자신)의 정보를 수정합니다. (현재는 닉네임 변경) ++추가

## **Friend** ++추가

- 친구 추가
    - 사용자가 다른 사용자에게 친구 추가 요청을 보냅니다.
- 친구 수락
    - 요청을 받은 사용자는 다른 사용자가 보낸 친구 요청을 수락합니다.
- 친구 요청 취소
    - 사용자가 다른 사용자에게 친구 추가 요청을 보낸 것을 취소합니다.
- 친구 요청 거절
    - 요청을 받은 사용자는 다른 사용자가 보낸 친구 요청을 거절합니다.
- 친구 삭제
    - 대상과 친구 관계(친구, 친구 요청 중, 친구 수락 받기 전..)를 끊습니다.(삭제)
- 친구 목록 조회
    - 로그인한 사용자의 친구 목록을 봅니다.

## **Chat**

- 채팅방 생성
    - 채팅방 사용자끼리의 채팅할 공간이 있어야 합니다. 이때, 사용자는 채팅방을 생성할 수 있습니다.
- 채팅방 제목 수정 ++추가
    - 채팅방 방장만이 채팅방의 제목을 변경할 수 있습니다.
- 채팅방 목록 조회
    - 현재 채팅방의 목록을 조회할 수 있어야 합니다.
    - 사용자(자신)가 만든 채팅방 목록을 조회할 수 있어야 합니다.
    - 사용자(자신)가 들어간(가입) 채팅방 목록을 조회할 수 있어야 합니다.
- 채팅방 입장
    - 채팅방의 key를 통해 입장할 수 있습니다
- 채팅
    - 채팅할 수 있습니다.
- 채팅 조회
    - 이전에 했던 채팅들을 조회할 수 있어야 합니다.
- 채팅방 나가기
    - 구성원만 채팅방을 나갈 수 있습니다.
    - 사용자(자신)가 들어간(가입) 채팅방을 나갈 수 있습니다.
- 채팅방 삭제
    - 채팅방을 생성한 사용자만 삭제할 수 있습니다.

# ERD ver 1.0 
![image](https://user-images.githubusercontent.com/119172260/236689573-141d01e8-7992-4827-8721-f2ca09adb72e.png)
## ERD ver 1.x
![스크린샷 2023-05-31 194037](https://github.com/yeb0/NewChat/assets/119172260/a2bd1f39-afd7-4d23-acc0-077dcae78e61)

# Architecture ver 1.0


![version1 drawio (1)](https://user-images.githubusercontent.com/119172260/235178812-62b8b029-537b-4670-a387-cb2bd3b7432c.png)
# Architecture ver 2.0 (~ing)

![image](https://github.com/yeb0/NewChat/assets/119172260/aa8a9a1a-1ef4-4311-b699-4e39ab8d8ec5)



# Stack ⚒️
- Java 11
- Spring boot
- Gradle
- MySQL
- Web Socket
- Redis
- Nginx

# API 💻

[My API ver 1.0](https://www.notion.so/NewChat-147b4c7ceb5f48d0911f4b7af08dbd66?pvs=4#d2e4e87a42504e389faedd257e1aac15)

# ERD 🖇️

[My ERD ver 1.0](https://github.com/yeb0/NewChat/wiki/Architecture,-ERD-ver-1.0)

# WIKI 📜
[My WIKI](https://github.com/yeb0/NewChat/wiki)

