## 항해99 실전 프로젝트 (BlueMoon diary)
<img src="https://user-images.githubusercontent.com/79817823/170234163-15484676-6d55-45e3-a156-d9f579106538.jpg" width="300" height="300"/>


### 🔗 사이트
[Blue Moon diary](https://bluemoondiary.com)


### 🏠 소개
- Bluemoon 서비스는 <b>익명</b> & <b>다이어리 </b> 서비스입니다.
- 익명으로 자신의 다이어리를 작성하고 고민을 서로 나눌 수 있습니다.
- 주요기능
  1. WebSocket을 이용한 실시간 채팅 및 알림
  2. Web RTC를 이용한 음성녹음 및 재생


### ⏲️ 개발기간
2022년 04월 22일 ~ 2022년 06월 3일


### 🧙 맴버구성
|  이름  |  포지션  |
| :----: | :-----: |
| [김승민](https://github.com/TodayIsYolo)|BE / Spring|
| [장재영](https://github.com/jaeyoungjang2)|BE / Spring|
| [최봉진](https://github.com/cbjjzzang)|BE / Spring|


### 👉 서비스 아키텍쳐
![image](https://user-images.githubusercontent.com/81352857/170249061-0e995564-d8f1-454c-8273-9dff5fc104e4.png)

### 👉 ERD
![image](https://user-images.githubusercontent.com/47101085/170249038-6053bdbc-516f-457d-901e-ab224eace724.png)

### 💻 기술 스택
<img src="https://img.shields.io/badge/Java-007396?style=flat-square&logo=Java&logoColor=white"/> <img src="https://img.shields.io/badge/Spring Boot-6DB33F?style=flat-square&logo=Spring Boot&logoColor=white"/> <img src="https://img.shields.io/badge/Spring Security-6DB33F?style=flat-square&logo=Spring Security&logoColor=white"/>

<img src="https://img.shields.io/badge/Stomp-010101?style=flat-square&logo=Stomp&logoColor=white"/> <img src="https://img.shields.io/badge/Socket-010101?style=flat-square&logo=Socket.io&logoColor=white"/> <img src="https://img.shields.io/badge/Redis-DC382D?style=flat-square&logo=Redis&logoColor=white"/> <img src="https://img.shields.io/badge/Sourcetree-0052CC?style=flat-square&logo=Sourcetree&logoColor=white"/> <img src="https://img.shields.io/badge/MySQL-4479A1?style=flat-square&logo=MySQL&logoColor=white"/>

<img src="https://img.shields.io/badge/GitHub-181717?style=flat-square&logo=GitHub&logoColor=white"/> <img src="https://img.shields.io/badge/Amazon S3-569A31?style=flat-square&logo=Amazon S3&logoColor=white"/> <img src="https://img.shields.io/badge/Amazon AWS-232F3E?style=flat-square&logo=Amazon AWS&logoColor=white"/> <img src="https://img.shields.io/badge/NGINX-009639?style=flat-square&logo=NGINX&logoColor=white"/> <img src="https://img.shields.io/badge/Docker-2496ED?style=flat-square&logo=Docker&logoColor=white"/>


### 📝 공통 문서
- **ERD(Entity Relationship Diagram)** - <a href="https://github.com/hh99-Final-Project/bluemoonBE/wiki/ERD" > WIKI 이동</a>
- **API(Application Programming Interface)** - <a href="https://github.com/hh99-Final-Project/bluemoonBE/wiki/API" > WIKI 이동</a>
- **Error Code**<a href="https://github.com/hh99-Final-Project/bluemoonBE/wiki/Errorcode" > WIKI 이동</a>
- **JWT 및 이중로그인 방지** - <a href="https://github.com/hh99-Final-Project/bluemoonBE/wiki/JWT-%EB%B0%8F-%EC%9D%B4%EC%A4%91%EB%A1%9C%EA%B7%B8%EC%9D%B8-%EB%B0%A9%EC%A7%80" > WIKI 이동</a>
- **Web Socket** - <a href="https://github.com/hh99-Final-Project/bluemoonBE/wiki/WebSocket%EC%9D%84-%EC%9D%B4%EC%9A%A9%ED%95%9C-%EC%8B%A4%EC%8B%9C%EA%B0%84-%EC%B1%84%ED%8C%85-%EB%B0%8F-%EC%95%8C%EB%A6%BC"> WIKI 이동</a>
- **대댓글** - <a href="https://github.com/hh99-Final-Project/bluemoonBE/wiki/%EB%8C%80%EB%8C%93%EA%B8%80"> WIKI 이동</a>
- **추첨 및 포인트** - <a href="https://github.com/hh99-Final-Project/bluemoonBE/wiki/%EC%B6%94%EC%B2%A8-%EB%B0%8F-%ED%8F%AC%EC%9D%B8%ED%8A%B8"> WIKI 이동</a>


### ⚙️ 개발 환경
- **Server** : AWS EC2(Linux 2 AMI)
- **Framework** : Springboot
- **Database** : Mysql (AWS RDS), Redis (Aws ElastiCache)
- **ETC** : AWS S3, AWS IAM, AWS Parameter Store, AWS Code deploy, Docker hub, NginX


### 💥 트러블 슈팅
- **N+1 문제** - <a href="https://github.com/hh99-Final-Project/bluemoonBE/wiki/N---1-%EB%AC%B8%EC%A0%9C-%ED%95%B4%EA%B2%B0%EC%9D%84-%EC%9C%84%ED%95%9C-@EntityGraph-%EB%8F%84%EC%9E%85"> WIKI 이동</a>
- **Jasypt를 이용한 암호화 적용시 발생한 문제** - <a href="https://github.com/hh99-Final-Project/bluemoonBE/wiki/Jasypt%EB%A5%BC-%EC%9D%B4%EC%9A%A9%ED%95%9C-%EC%A3%BC%EC%9A%94%EC%A0%95%EB%B3%B4-%EC%95%94%ED%98%B8%ED%99%94"> WIKI 이동</a>
- **Spring Security에서 전역 예외처리가 안되는 문제** - <a href="https://github.com/hh99-Final-Project/bluemoonBE/wiki/Spring-security%EC%97%90%EC%84%9C%EC%9D%98-%EC%98%88%EC%99%B8%EC%B2%98%EB%A6%AC"> WIKI 이동</a>
- **Access Token의 유효기간 설정에 따른 보안 문제** - <a href="https://github.com/hh99-Final-Project/bluemoonBE/wiki/%F0%9F%92%A5%ED%8A%B8%EB%9F%AC%EB%B8%94-%EC%8A%88%ED%8C%85-(Refresh-Token)"> WIKI 이동</a>
- **이벤트 참여 횟수를 매일 초기화 하는 문제** - <a href="https://github.com/hh99-Final-Project/bluemoonBE/wiki/%EC%9D%B4%EB%B2%A4%ED%8A%B8-%EC%B0%B8%EC%97%AC-%ED%9A%9F%EC%88%98%EB%A5%BC-%EB%A7%A4%EC%9D%BC-%EC%B4%88%EA%B8%B0%ED%99%94-%ED%95%B4%EC%95%BC-%EB%90%98%EB%8A%94-%EB%AC%B8%EC%A0%9C"> WIKI 이동</a>
