# KDT 두번째 프로젝트 BACKEND
  
## 개발환경
  
IntelliJ IDEA  
Spring Boot version '3.4.1'  
  
## dependencies
  
```declarative
	implementation 'org.springframework.boot:spring-boot-starter-web'
	implementation 'org.springframework.boot:spring-boot-starter-jdbc'
	runtimeOnly 'org.mariadb.jdbc:mariadb-java-client'
//	runtimeOnly 'com.mysql:mysql-connector-j'
	compileOnly 'org.projectlombok:lombok'
	developmentOnly 'org.springframework.boot:spring-boot-devtools'
	annotationProcessor 'org.projectlombok:lombok'
	testImplementation 'org.springframework.boot:spring-boot-starter-test'
	testImplementation 'org.springframework.security:spring-security-test'
	testRuntimeOnly 'org.junit.platform:junit-platform-launcher'
	implementation 'jakarta.persistence:jakarta.persistence-api:3.1.0'
	implementation 'org.springframework.boot:spring-boot-starter-security'
	implementation 'org.thymeleaf.extras:thymeleaf-extras-springsecurity6:3.1.2.RELEASE'
```
  
  
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
  
### ManyToOne + @OneToMany 양방향 관계 순환 참조 문제
  
Jackson을 사용하여 JSON 직렬화를 수행할 때, 무한 루프가 발생할 가능성이 있음.  
