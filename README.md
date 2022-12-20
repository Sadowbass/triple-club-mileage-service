# 트리플 클럽 마일리지 서비스

## 목차

1. [개발 및 실행 환경](#개발-및-실행-환경)
2. [실행 방법](#실행-방법)
3. [설계 및 개발 의도](#설계-및-개발-의도)
4. [API 명세서](#API-명세서)
5. [애플리케이션내에서 발생하는 쿼리와 추가로 고려해볼 Index](#애플리케이션내에서-발생하는-쿼리와-추가로-고려해볼-Index)

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
    1. 대부분의 설정은 1번과 동일하나 기본적으로 hibernate의 ddl-auto가 create로 설정되어있어 빌드때마다 테스트 코드가 자동으로 실행되며 테스트 db의 스키마를 건드리게 됨으로 원치 않을경우
       ddl-auto를 none으로 바꾸어야 합니다
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
2. 외래키는 논리적으로 존재하나 운영 및 개발의 편의성을 위해 물리적인 제약조건을 걸지 않았습니다. 개인적으로는 프로젝트가 크나 작으나 RDB를 이용한다면 외래키를 물리적으로 걸어주는것이 좋다고 생각합니다.
3. JPA를 사용하며 발생하는 쿼리들의 실행계획을 모두 확인하였으며 const, ref, eq_ref정도로 실행 계획이 세워졌을경우에는 커버링 인덱스를 위한 인덱스를 별도로 생성하지 않았습니다. 인덱스와 쿼리튜닝은
   데이터 건수, 조회와 CUD 요청량에따라 유동적으로 설정하는것이 용이하다고 생각했습니다.
4. userId, placeId, reviewId 이 세가지 UUID는 pk로 이용할 수도 있으나 순차적으로 발생하는것이 아니기에 클러스터 인덱스는 MySql의 auto_increment 값을 이용하고 UUID는
   unique not null로 mysql에서 자동으로 생성해주는 인덱스를 이용했습니다.
5. 과제물 내용을 참고하여 하나의 커다란 모놀리틱 서비스가 아닌 사용자, 장소, 리뷰, 마일리지등이 각각 분리되어있는 MSA 환경으로 생각하고 개발하였습니다.
6. 최대한 도메인 중심으로 개발하고자 하였으며 서로 다른 애그리거트간에 어떠한 처리가 되는 일이 없도록 고려하였습니다.

<br>

## API 명세서

### `POST /events`

#### 리뷰 작성이 이뤄질때마다 발생하는 API 입니다.

| Method | Path    | Content-Type     |
| :----: | :---:   | :----------:     |
| POST   | /events | application/json |  

`Request Data (HTTP body)`

| field name       | data type  | required | values           | description                                 | 
| :----:           | :----:     | :----:   | :----:           | :-----:                                     |
| type             | string     | true     | REVIEW           | 이벤트의 종류를 정합니다                    |
| action           | string     | true     | ADD, MOD, DELETE | 이벤트의 동작을 정합니다                    |
| reviewId         | UUID       | true     | -                | 해당 리뷰의 UUID 형식의 ID입니다            |
| content          | string     | false    | -                | 리뷰의 내용입니다                           |
| attachedPhotoIds | list<UUID> | false    | -                | 리뷰에 포함된 사진들의 UUID 형식의 ID입니다 |
| userId           | UUID       | true     | -                | 리뷰를 작성한 사용자의 UUID 형식의 ID입니다 |
| placeId          | UUID       | true     | -                | 리뷰를 작성한 장소의 UUID 형식의 ID입니다   |

`Response Data (success)`

| field name        | type       | required | description                                        |
| :----:            | :----:     | :----:   | :-----:                                            |
| rspCode           | number     | true     | 응답상태에 대한 코드입니다                         |
| rspMessage        | string     | true     | 응답상태에 대한 메세지 입니다                      |
| result            | object     | true     | 사용자의 마일리지 정보를 담은 object 입니다        |       
| * userId          | UUID       | true     | 사용자의 UUID 형식의 ID입니다                      |
| * numberOfReviews | number     | true     | 사용자가 작성한 리뷰의 숫자입니다                  |       
| * mileage         | number     | true     | 사용자의 현재 총 마일리지입니다                    |
| * detailPath      | URI        | true     | 사용자 마일리지 상세항목을 요청하는 API 경로입니다 |

`Response Data (Failed)`

| field name        | type       | required | description                                        |
| :----:            | :----:     | :----:   | :-----:                                            |
| rspCode           | number     | true     | 응답상태에 대한 코드입니다                         |
| rspMessage        | string     | true     | 응답상태에 대한 메세지 입니다                      |

`Request Example`

```json
{
  "type": "REVIEW",
  "action": "ADD",
  "reviewId": "240a0658-dc5f-4878-9381-ebb7b2667772",
  "content": "좋아요!",
  "attachedPhotoIds": [
    "e4d1a64e-a531-46de-88d0-ff0ed70c0bb8",
    "afb0cef2-851d-4a50-bb07-9cc15cbdc332"
  ],
  "userId": "3ede0ef2-92b7-4817-a5f3-0c575361f745",
  "placeId": "2e4baf1c-5acb-4efb-a1af-eddada31b00f"
}
```

`Response Example`

```json
{
  "rspCode": 0,
  "rspMessage": "Request Success.",
  "result": {
    "userId": "3ede0ef2-92b7-4817-a5f3-0c575361f745",
    "numberOfReviews": 1,
    "mileage": 2,
    "detailPath": "/mileage/3ede0ef2-92b7-4817-a5f3-0c575361f745/details?pageNum=1"
  }
}
```

<br>

### `GET /mileage/{userId}`

#### 사용자의 현재 보유중인 총 마일리지를 조회합니다.

| Method | Path              | Content-Type     |
| :----: | :---:             | :----------:     |
| GET    | /mileage/{userId} | -                |  

`Request Data`

| field name       | type       | required | values           | description                                 | 
| :----:           | :----:     | :----:   | :----:           | :-----:                                     |
| userId           | UUID       | true     | -                | 사용자의 UUID 형식의 ID입니다 (path var)    |

`Response Data (success)`

| field name        | type       | required | description                                        |
| :----:            | :----:     | :----:   | :-----:                                            |
| rspCode           | number     | true     | 응답상태에 대한 코드입니다                         |
| rspMessage        | string     | true     | 응답상태에 대한 메세지 입니다                      |
| result            | object     | true     | 사용자의 마일리지 정보를 담은 object 입니다        |       
| * userId          | UUID       | true     | 사용자의 UUID 형식의 ID입니다                      |
| * numberOfReviews | number     | true     | 사용자가 작성한 리뷰의 숫자입니다                  |       
| * mileage         | number     | true     | 사용자의 현재 총 마일리지입니다                    |
| * detailPath      | URI        | true     | 사용자 마일리지 상세항목을 요청하는 API 경로입니다 |

`Response Data (Failed)`

| field name        | type       | required | description                                        |
| :----:            | :----:     | :----:   | :-----:                                            |
| rspCode           | number     | true     | 응답상태에 대한 코드입니다                         |
| rspMessage        | string     | true     | 응답상태에 대한 메세지 입니다                      |

`Request Example`

`https://localhost:8080/mileage/3ede0ef2-92b7-4817-a5f3-0c575361f745`

`Response Example`

```json
{
  "rspCode": 0,
  "rspMessage": "Request Success.",
  "result": {
    "userId": "3ede0ef2-92b7-4817-a5f3-0c575361f745",
    "numberOfReviews": 0,
    "mileage": 0,
    "detailPath": "/mileage/3ede0ef2-92b7-4817-a5f3-0c575361f745/details?pageNum=1"
  }
}
```

### `GET /mileage/{userId}/details`

#### 사용자의 마일리지 적립 이력을 조회합니다 (삭제한 리뷰의 마일리지 이력 포함)

| Method | Path                      | Content-Type     |
| :----: | :---:                     | :----------:     |
| GET    | /mileage/{userId}/details | -                |

`Request Data`

| field name       | type       | required | values           | description                                        | 
| :----:           | :----:     | :----:   | :----:           | :-----:                                            |
| userId           | UUID       | true     | -                | 사용자의 UUID 형식의 ID입니다 (path var)           |
| pageNum          | number     | false    | -                | 상세확인 페이지 번호입니다. (parameter, 기본값 1)  |
| order            | string     | false    | asc, desc        | 등록일자 정렬방식입니다. (parameter, 기본값 desc)  |

`Response Data (success)`

| field name        | type         | required | description                                        |
| :----:            | :----:       | :----:   | :-----:                                            |
| rspCode           | number       | true     | 응답상태에 대한 코드입니다                         |
| rspMessage        | string       | true     | 응답상태에 대한 메세지 입니다                      |
| result            | object       | true     | 사용자의 마일리지 상세 정보를 담은 object 입니다   |       
| * userId          | UUID         | true     | 사용자의 UUID 형식의 ID입니다                      |
| * currentPage     | number       | true     | 마일리지 상세보기의 현재 페이지입니다              |       
| * lastPage        | number       | true     | 사용자 마일리지 최종페이지 번호입니다              |
| * totalCount      | number       | true     | 마일리지 변동이력 총 건수입니다                    |
| * details         | list<object> | false    | 마일리지 상세내역 입니다                           |
| ** reviewId       | UUID         | true     | 이벤트가 발생한 리뷰의 id입니다                    |
| ** eventAction    | string       | true     | 이벤트의 동작입니다                                |
| ** changedMileage | number       | true     | 변동된 마일리지 입니다                             |
| ** reason         | string       | true     | 변동된 사유입니다                                  |
| ** createdAt      | datetime     | true     | 변동된 일시입니다.                                 |

`Response Data (Failed)`

| field name        | type       | required | description                                        |
| :----:            | :----:     | :----:   | :-----:                                            |
| rspCode           | number     | true     | 응답상태에 대한 코드입니다                         |
| rspMessage        | string     | true     | 응답상태에 대한 메세지 입니다                      |

`Request Example`

`localhost:8080/mileage/3ede0ef2-92b7-4817-a5f3-0c575361f745/details?pageNum=1&order=desc`

`Response Example`

```json
{
  "rspCode": 0,
  "rspMessage": "Request Success.",
  "result": {
    "userId": "3ede0ef2-92b7-4817-a5f3-0c575361f745",
    "currentPage": 0,
    "lastPage": 1,
    "totalCount": 11,
    "details": [
      {
        "reviewId": "240a0658-dc5f-4878-9381-ebb7b2667772",
        "eventAction": "MOD",
        "changedMileage": -1,
        "reason": "photo",
        "createdAt": "2022-12-20 21:03:25"
      },
      {
        "reviewId": "240a0658-dc5f-4878-9381-ebb7b2667772",
        "eventAction": "MOD",
        "changedMileage": -1,
        "reason": "content",
        "createdAt": "2022-12-20 21:03:24"
      },
      {
        "reviewId": "240a0658-dc5f-4878-9381-ebb7b2667772",
        "eventAction": "MOD",
        "changedMileage": 1,
        "reason": "content",
        "createdAt": "2022-12-20 21:03:22"
      },
      {
        "reviewId": "240a0658-dc5f-4878-9381-ebb7b2667772",
        "eventAction": "MOD",
        "changedMileage": 1,
        "reason": "photo",
        "createdAt": "2022-12-20 21:03:21"
      },
      {
        "reviewId": "240a0658-dc5f-4878-9381-ebb7b2667772",
        "eventAction": "MOD",
        "changedMileage": -1,
        "reason": "photo",
        "createdAt": "2022-12-20 21:03:21"
      },
      {
        "reviewId": "240a0658-dc5f-4878-9381-ebb7b2667772",
        "eventAction": "MOD",
        "changedMileage": -1,
        "reason": "content",
        "createdAt": "2022-12-20 21:03:20"
      },
      {
        "reviewId": "240a0658-dc5f-4878-9381-ebb7b2667772",
        "eventAction": "MOD",
        "changedMileage": 1,
        "reason": "content",
        "createdAt": "2022-12-20 21:03:19"
      },
      {
        "reviewId": "240a0658-dc5f-4878-9381-ebb7b2667772",
        "eventAction": "MOD",
        "changedMileage": 1,
        "reason": "photo",
        "createdAt": "2022-12-20 21:03:18"
      },
      {
        "reviewId": "240a0658-dc5f-4878-9381-ebb7b2667772",
        "eventAction": "MOD",
        "changedMileage": -1,
        "reason": "photo",
        "createdAt": "2022-12-20 21:03:16"
      },
      {
        "reviewId": "240a0658-dc5f-4878-9381-ebb7b2667772",
        "eventAction": "MOD",
        "changedMileage": -1,
        "reason": "content",
        "createdAt": "2022-12-20 21:03:13"
      },
      {
        "reviewId": "240a0658-dc5f-4878-9381-ebb7b2667772",
        "eventAction": "ADD",
        "changedMileage": 3,
        "reason": "new review",
        "createdAt": "2022-12-20 21:02:57"
      }
    ]
  }
}
```

### 응답코드

| code  | description                                                                   |
| :---: | :---------:                                                                   |
| 0     | 정상입니다                                                                    |
| 10~19 | 요청 메세지는 정상이나 처리중 문제가 발생하는 요청값들에 대한 에러범위 입니다 |
| 20~29 | 요청 메세지 자체의 에러범위입니다                                             |
| 99    | 처리중 발생한 내부 에러로 인한 응답입니다. 로그를 확인하세요                  |

| code  | message                                            | http status | description                                     |
| :---: | :-----:                                            | :---------: | :---------:                                     | 
| 0     | Request Success.                                   | 200         | 요청이 정상처리 되었습니다                      |       
| 11    | Cannot find user. userId : {userId}                | 400         | 요청한 사용자 Id가 없습니다                     |
| 12    | Cannot find place. placeId : {placeId}             | 400         | 요청한 장소 Id가 없습니다                       |
| 13    | Cannot find review. review Id : {reviewId}         | 400         | 수정, 삭제시 요청한 리뷰 Id가 없습니다          |    
| 14    | User already review this place. {userId} {placeId} | 400         | 해당 장소에 이미 사용자가 리뷰를 하였습니다     |    
| 15    | Duplicated review id. {reviewId}                   | 400         | 신규 리뷰 등록시 리뷰 Id가 중복입니다           |    
| 16    | User and reviewed user are different               | 400         | 리뷰를 작성한 사용자와 요청한 사용자가 다릅니다 |
| 17    | Review already deleted                             | 400         | 이미 삭제된 리뷰를 삭제 요청 하였습니다         |
| 21    | Empty required field. field : {fieldName}          | 400         | 필수값 필드가 누락되었습니다                    |
| 22    | Wrong required value. field : {fieldName}          | 400         | 형태 혹은 사용 할 수 없는값을 요청하였습니다    |
| 23    | Wrong request message                              | 400         | 요청 메세지 포멧이 잘못되었습니다               |
| 99    | Internal server error                              | 500         | 처리중 내부 서버에러입니다 로그를 확인하세요    |
