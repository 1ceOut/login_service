# 로그인 서비스
- 저희 프로젝트의 목적은 사용자 인증을 간편하게 구현하여 좀 더 많은 사용자가 편하고 쉽게 사용할 수 있게 다양한 소셜 로그인 기능을 REST API와 JWT방식을 채택하여 구현하였습니다.

## JWT+REST API의 장점
- 무상태성, 다양한 클라이언트 지원, 간편한 인증관리, 보안성 등을 확보
- 로그인 서비스가 다른 서비스와 분리되어 있어도 문제 없이 작동하는 장점을 추가로 지님
- 서비스의 유지보수 및 확장성을 높일 수 있다

## MSA 관점에서 바라보는 장점
- 다른 마이크로서비스 간에 인증 정보를 공유할 필요가 없어, 각 서비스가 독립적으로 인증을 처리할 수 있고 이로 인해 보안성을 높이고 서비스 간의 결합도를 줄일 수 있다는 장점을 지닐수 있다는 점에서 해당 방식을 채택하게 되었습니다.

## 보완성 문제
- 대표적으로 CSRF(사이트간 요청 위조)공격과 XSS(교차사이트스크립트)공격이 있을수 있습니다.
- CSRF 보완
  - SameSite 속성을 설정하여 타 도메인에서의 요청을 차단하여 해결
- XSS 보완
  - HttpOnly 및 Secure을 설정하여 XSS취약점을 보완

## Apache Kafka를 활용한 타 DB 동기화
- 유저 정보를 Neo4j와 동기화할 필요성을 느껴 메세징 시스템인 Apache Kafka를 활용하였습니다.
- Kafka Connector를 활용하여 새로운 유저의 정보가 Mysql에 저장되거나 재로그인을 통해 로그인이 진행되고 유저의 정보가 수정될 경우, Neo4j와 동기화 시켰습니다.

## REST API방식의 타 마이크로 서비스들과의 데이터 공유
- 다른 마이크로 서비스와의 동기화를 위해 Kafka를 활용하지 않고 REST API방식을 채택하여 통신을 진행하였습니다.
- Spring Cloud Openfeign을 사용하여 해당 마이크로 서비스에 요청하여 유저의 데이터를 제공 받을 수 있습니다.
- 해당 서비스의 REST API에 접근성과 개발에 대한 편의성을 높이기 위해 Swagger를 활용하여 API Docs를 작성하고 공유하여 작업의 효율성을 극대화 하였습니다.
