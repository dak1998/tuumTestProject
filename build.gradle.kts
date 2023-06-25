plugins {
	java
	id("org.springframework.boot") version "3.1.0"
	id("io.spring.dependency-management") version "1.1.0"
}

group = "com.tuum"
version = "0.0.1-SNAPSHOT"

java {
	sourceCompatibility = JavaVersion.VERSION_17
}

configurations {
	compileOnly {
		extendsFrom(configurations.annotationProcessor.get())
	}
}

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-amqp")
	implementation("org.mybatis.spring.boot:mybatis-spring-boot-starter:3.0.2")
	implementation("org.mybatis.dynamic-sql:mybatis-dynamic-sql:1.5.0")
	implementation("org.postgresql:postgresql:42.6.0")
	implementation("org.springframework:spring-jdbc:6.0.10")
	implementation("org.springframework.boot:spring-boot-starter-web:3.1.0")
	implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.1.0")
	compileOnly("org.projectlombok:lombok")
	annotationProcessor("org.projectlombok:lombok")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("org.springframework.boot:spring-boot-starter-data-jpa")
	testImplementation("org.springframework.amqp:spring-rabbit-test")
	testImplementation("com.h2database:h2:2.1.214")
}

tasks.withType<Test> {
	useJUnitPlatform()
}
