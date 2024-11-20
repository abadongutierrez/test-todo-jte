import kotlin.io.path.Path

plugins {
    kotlin("jvm") version "1.9.25"
    kotlin("plugin.spring") version "1.9.25"
    id("org.springframework.boot") version "3.3.5"
    id("io.spring.dependency-management") version "1.1.6"
    id("gg.jte.gradle") version "3.1.12"
}

group = "com.jabaddon.learning.jte"
version = "0.0.1-SNAPSHOT"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("gg.jte:jte:3.1.12")
    implementation("gg.jte:jte-kotlin:3.1.12")
    implementation("gg.jte:jte-spring-boot-starter-3:3.1.12")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")

    implementation("de.tschuehly:spring-view-component-jte:0.8.3")
    // Add Spring Boot DevTools
    developmentOnly("org.springframework.boot:spring-boot-devtools")
}

kotlin {
    compilerOptions {
        freeCompilerArgs.addAll("-Xjsr305=strict")
    }
}

jte {
    generate()
    binaryStaticContent = true
    //sourceDirectory.set(Path("src/main/kotlin"))
}

tasks.withType<Test> {
    useJUnitPlatform()
}
