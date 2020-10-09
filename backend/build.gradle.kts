plugins {
    id("org.springframework.boot") version "2.1.3.RELEASE"
    id("io.spring.dependency-management") version "1.0.8.RELEASE"
    kotlin("jvm") version "1.3.50"
    kotlin("plugin.spring") version "1.3.50"
}

group = "ru.made"
version = "flitter"
java.sourceCompatibility = JavaVersion.VERSION_1_8

repositories {
    mavenCentral()
    maven {
        url = uri("https://plugins.gradle.org/m2/")
    }
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-actuator:2.3.4.RELEASE")
    implementation("org.springframework.boot:spring-boot-starter-web:2.3.4.RELEASE")
    testImplementation("org.springframework.boot:spring-boot-starter-test:2.3.4.RELEASE") {
        exclude(group = "org.junit.vintage", module = "junit-vintage-engine")
    }
//    Uncomment this dependency in order to include frontend files to your app
//    runtimeOnly(project(":frontend"))
}