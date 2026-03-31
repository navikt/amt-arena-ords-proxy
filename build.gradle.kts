import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    val kotlinVersion = "2.3.20"

    id("org.springframework.boot") version "4.0.4"
    id("io.spring.dependency-management") version "1.1.7"

    kotlin("jvm") version kotlinVersion
    kotlin("plugin.spring") version kotlinVersion
}

group = "no.nav.amt-arena-ords-proxy"
version = "0.0.1"
description = "Proxy mot Arena ORDS i FSS"

repositories {
    mavenCentral()
    maven { url = uri("https://github-package-registry-mirror.gc.nav.no/cached/maven-release") }
}

val jacksonModuleKotlinVersion = "3.1.0"
val commonVersion = "3.2026.03.04_12.35-b34c347c6239"
val tokenValidationVersion = "6.0.4"
val okHttpVersion = "5.3.2"

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-logging")

    implementation("tools.jackson.module:jackson-module-kotlin:$jacksonModuleKotlinVersion")

    implementation("no.nav.common:log:$commonVersion")

    implementation("no.nav.common:rest:$commonVersion") {
        exclude(group = "com.squareup.okhttp3", module = "okhttp")
    }

    implementation("no.nav.security:token-validation-spring:$tokenValidationVersion")

    implementation("com.squareup.okhttp3:okhttp-jvm:$okHttpVersion")

    testImplementation("org.springframework.boot:spring-boot-starter-test")

    testImplementation("com.squareup.okhttp3:mockwebserver:$okHttpVersion")
}

kotlin {
    jvmToolchain(25)

    compilerOptions {
        jvmTarget = JvmTarget.JVM_25
        freeCompilerArgs.addAll(
            "-Xjsr305=strict",
            "-Xannotation-default-target=param-property",
        )
    }
}

tasks.named<Test>("test") {
    useJUnitPlatform()
}

tasks.named<Jar>("jar") {
    enabled = false
}
