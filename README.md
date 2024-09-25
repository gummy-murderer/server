# Gummy-murder - Server

[📌 Swagger-ui](http://ec2-15-165-15-244.ap-northeast-2.compute.amazonaws.com:8081/swagger-ui/)<br>

## 1. 🐻서비스 소개
**탐정이 되어 마을의 평화를 지켜라!**

- 베어머더러는 AI를 활용한 추리 게임 입니다.
- 탐정이 마을의 살인 사건 범인을 잡기 위해 증거를 조사하고 주민과 대화하며 매일 밤 1명을 지목하여 취조를 진행합니다.
<br/>

## 2. 📝주요 기능 소개

1) AI NPC와의 Chat을 통하여 마을 NPC들의 특징 및 알리바이 파악

2) 게임의 스토리 진행을 위한 Intro와 Scenario, alibi, FinalWords 생성 및 통신

3) 유저의 캐릭터 개성을 반영하는 캐릭터 커스텀 진행 

<br/>

## 3. 💡BackEnd 구현 기능
### Game
- 게임의 핵심 요소인 AI와 Unity 간의 통신을 구현 하여 두 시스템을 원활하게 연동
- 게임의 스토리 진행을 위한 Intro와 Scenario, Alibi, FinalWords 통신 코드 구현
- 유저의 게임 진행 상황을 관리하는 체크리스트 저장 및 전송 통신 구현, 유저의 게임 진행 편의성 제공

### Chat
- USER-NPC 및 NPC-NPC 채팅 시스템 개발

<br/>

## 4. 💻기술 스택

### Language
<img src="https://img.shields.io/badge/java-007396?style=for-the-badge&logo=java&logoColor=white"> 

### Framework And Libraries
![Spring](https://img.shields.io/static/v1?style=for-the-badge&message=Spring&color=6DB33F&logo=Spring&logoColor=FFFFFF&label=)
![Spring Boot](https://img.shields.io/static/v1?style=for-the-badge&message=Spring+Boot&color=6DB33F&logo=Spring+Boot&logoColor=FFFFFF&label=)
![Spring Security](https://img.shields.io/static/v1?style=for-the-badge&message=Spring+Security&color=6DB33F&logo=Spring+Security&logoColor=FFFFFF&label=)
![JWT](https://img.shields.io/badge/JWT-black?style=for-the-badge&logo=JSON%20web%20tokens)


### Database
![MySQL](https://img.shields.io/badge/mysql-%2300f.svg?style=for-the-badge&logo=mysql&logoColor=white)


### Servers
![AWS](https://img.shields.io/badge/AWS-%23FF9900.svg?style=for-the-badge&logo=amazon-aws&logoColor=white)

### Environment And Communication
![IntelliJ](https://img.shields.io/badge/IntelliJ-000000?style=for-the-badge&logo=intellijidea&logoColor=white)
![GitHub](https://img.shields.io/badge/Github-181717?style=for-the-badge&logo=Github&logoColor=white)
![Notion](https://img.shields.io/badge/Notion-000000?style=for-the-badge&logo=Notion&logoColor=white)

<br/>

## 📜Git Convention
| **Convention**  | **내용**                                                         |
|-----------------|----------------------------------------------------------------|
| **Feat**        | 새로운 기능 추가                                                      |
| **BugFix**         | 버그 수정                                                          |
| **Test**        | 테스트 코드, 리펙토링 테스트 코드 추가, Production Code(실제로 사용하는 코드) 변경 없음     |
| **Rename**      | 파일 혹은 폴더명을 수정하거나 옮기는 작업만인 경우                                   |
| **Remove**      | 파일을 삭제하는 작업만 수행한 경우                                            |
| **Refactor** | 프로덕션 코드 리팩토링                                                   |
| **Chore** | 빌드 관련 수정, 패키지 관리자,yml, 구성 등 업데이트                          |


