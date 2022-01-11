import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	id("org.springframework.boot") version "2.6.2"
	id("io.spring.dependency-management") version "1.0.11.RELEASE"
	kotlin("jvm") version "1.6.10"
	kotlin("plugin.spring") version "1.6.10"
	kotlin("plugin.serialization") version "1.6.10"
}

group = "com.gierre"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_17

configurations {
	compileOnly {
		extendsFrom(configurations.annotationProcessor.get())
	}
}

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-thymeleaf:2.6.2")
	implementation("org.springframework.boot:spring-boot-starter-web:2.6.2")
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.13.1")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
	implementation("io.springfox:springfox-swagger2:2.9.2") // dont update to the lastest version
	implementation("io.springfox:springfox-swagger-ui:2.9.2") // dont update to the lastest version
	implementation("org.apache.poi:poi:5.1.0")
	implementation("org.apache.poi:poi-ooxml:5.1.0")
	implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.3.2")

	developmentOnly("org.springframework.boot:spring-boot-devtools:2.6.2")
	annotationProcessor("org.springframework.boot:spring-boot-configuration-processor:2.6.2")
	testImplementation("org.springframework.boot:spring-boot-starter-test:2.6.2")

}

tasks.withType<KotlinCompile> {
	kotlinOptions {
		freeCompilerArgs = listOf("-Xjsr305=strict")
		jvmTarget = "17"
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
}