import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    val kotlinVersion = "2.4.0"

    id("org.springframework.boot") version "4.1.0"
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

val jacksonModuleKotlinVersion = "3.2.1"
val commonVersion = "4.2026.06.25_10.50-baa9d54e3cd8"
val tokenValidationVersion = "6.0.11"
val okHttpVersion = "5.4.0"

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
