# 트리플 클럽 마일리지 서비스
## 목차 : 
1. 개발 및 실행 환경
2. 실행 방법
3. 설계 및 개발 의도
4. API 명세서
5. 애플리케이션내에서 발생하는 쿼리와 추가로 고려해볼 Index

<br>

## 개발 및 실행 환경
1. Java 11 (Azul zulu community 11.0.11)  
2. SpringBoot 2.7.6  
   > spring-boot-starter-web  
   > spring-boot-starter-data-jpa  
   > spring-boot-starter-aop
3. MySql 5.7.39  
4. QueryDSL-JPA 5.0.0  

<br>  

## 실행 방법  
1. src/main/resources/application.yml 파일을 설정합니다  
   1. 채용팀 담당자님께서 확인은 로컬DB로 하신다고 하시어 별도 인증서 및 연결 설정은 되어있지 않습니다.  
   2. spring.datasource.username, password, url에 db 연결정보를 입력합니다.  
   3. 로그는 실행 환경이 각각 다름으로 logback.xml로 별도 file-appender등을 설정하지 않고 콘솔에 출력합니다  
2. {project-root}/ddl/ddl.sql을 이용하여 테이블 및 기본 user와 place를 등록합니다.
3. Mac, Linux, Window 셋의 모든 로컬환경 실행은 gradlew bootRun으로 수행합니다.
   1. Mac
      ![Mac Example](https://github.com/Sadowbass/triple-club-mileage-service/blob/main/images/run_mac.png)
   2. Linux (Ubuntu 16(Azure vm))
      ![Linux Example](https://github.com/Sadowbass/triple-club-mileage-service/blob/main/images/run_ubuntu.png)
   3. Windows 11
      ![Window Example](https://github.com/Sadowbass/triple-club-mileage-service/blob/main/images/run_window.png)
