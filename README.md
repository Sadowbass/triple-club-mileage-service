# 트리플 클럽 마일리지 서비스
## 목차
1. [개발 및 실행 환경](#개발-및-실행-환경)
2. [실행 방법](#실행-방법)
3. [설계 및 개발 의도](#설계-및-개발-의도)
4. [API 명세서](API-명세서)
5. [애플리케이션내에서 발생하는 쿼리와 추가로 고려해볼 Index](애플리케이션내에서-발생하는-쿼리와-추가로-고려해볼-Index)

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
   1. 채용팀 담당자님께서 확인은 로컬 DB로 하신다고 하시어 별도 인증서 및 연결 설정은 되어있지 않습니다.  
   2. spring.datasource.username, password, url에 db 연결정보를 입력합니다.
   3. 로그는 실행 환경이 각각 다름으로 logback.xml로 별도 file-appender등을 설정하지 않고 콘솔에 출력합니다
2. src/test/resources/application.yml 파일을 설정합니다
   1. 대부분의 설정은 1번과 동일하나 기본적으로 hibernate의 ddl-auto가 create로 설정되어있어 빌드때마다
      테스트 코드가 자동으로 실행되며 테스트 db의 스키마를 건드리게 됨으로
      원치 않을경우 ddl-auto를 none으로 바꾸어야 합니다
3. {project-root}/ddl/ddl.sql을 이용하여 테이블 및 기본 user와 place를 등록합니다.
4. Mac, Linux, Window 셋의 모든 로컬환경 실행은 `gradlew bootRun`으로 수행합니다.
   1. Mac
      ![Mac Example](https://github.com/Sadowbass/triple-club-mileage-service/blob/main/images/run_mac.png)
   2. Linux (Ubuntu 16(Azure vm))
      ![Linux Example](https://github.com/Sadowbass/triple-club-mileage-service/blob/main/images/run_ubuntu.png)
   3. Windows 11
      ![Window Example](https://github.com/Sadowbass/triple-club-mileage-service/blob/main/images/run_window.png)

<br>

## 설계 및 개발 의도
1. 개인적으로 공부하던 코드나 방식보다는 은행에서 업무하던때처럼 보수적으로 코드를 짜고자 하였습니다.
2. 외래키는 논리적으로 존재하나 운영 및 개발의 편의성을 위해 물리적인 제약조건을 걸지 않았습니다.
   개인적으로는 프로젝트가 크나 작으나 RDB를 이용한다면 외래키를 물리적으로 걸어주는것이 좋다고 생각합니다.
3. JPA를 사용하며 발생하는 쿼리들의 실행계획을 모두 확인하였으며 const, ref, eq_ref정도로 실행 계획이 세워졌을경우에는 
   커버링 인덱스를 위한 인덱스를 별도로 생성하지 않았습니다. 인덱스와 쿼리튜닝은 데이터 건수, 조회와 CUD 요청량에따라
   유동적으로 설정하는것이 용이하다고 생각했습니다.
4. userId, placeId, reviewId 이 세가지 UUID는 pk로 이용할 수도 있으나 순차적으로 발생하는것이 아니기에
   클러스터 인덱스는 MySql의 auto_increment 값을 이용하고 UUID는 unique not null로 mysql에서 자동으로 생성해주는 인덱스를 이용했습니다.
5. 과제물 내용을 참고하여 하나의 커다란 모놀리틱 서비스가 아닌 사용자, 장소, 리뷰, 마일리지등이 각각 분리되어있는 MSA 환경으로 생각하고 개발하였습니다.
6. 최대한 도메인 중심으로 개발하고자 하였으며 서로 다른 애그리거트간에 어떠한 처리가 되는 일이 없도록 고려하였습니다.

<br>

## API 명세서
