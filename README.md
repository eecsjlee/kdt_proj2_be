# KDT 두번째 프로젝트 BACKEND
  
## 개발환경
  
IntelliJ IDEA  
Spring Boot version '3.4.1'  
MariaDB '10.11.10'
  
## 프로젝트 기간
  
2025-01-13 ~ 2025-02-20  
  
## 팀 구성
|      담당      |  이름   |             GITHUB              |
|:------------:|:-----:|:-------------------------------:|
|  FRONT-END   |  김좌현  |  https://github.com/jwahyunkim  |
|   BACK-END   |  이준영  |   https://github.com/eecsjlee   |
|     DATA     |  박래찬  |  https://github.com/chani1352   |
  
## 메모
  

## 트러블 슈팅
  
### @ManyToOne + @OneToMany 양방향 관계 순환 참조 문제
  
Jackson을 사용하여 JSON 직렬화를 수행할 때, 무한 루프가 발생할 가능성이 있음.  
  
### 정적 이미지 변경 후 서버 재시작 필요 문제
  
src/main/resources/static/ 폴더에 차량 번호판 이미지를 저장했으나, 프론트엔드에서 즉시 반영되지 않고 서버를 재시작해야만 변경사항이 보이는 문제 발생.
  