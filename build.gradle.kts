import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    val kotlinVersion = "2.4.20"

    id("org.springframework.boot") version "4.1.1"
    id("io.spring.dependency-management") version "1.1.7"

    kotlin("jvm") version kotlinVersion
    kotlin("plugin.spring") version kotlinVersion
}

group = "no.nav.amt-arena-ords-proxy"
version = "0.0.1"
description = "Proxy mot Arena ORDS i FSS"

repositories {
    mavenCentral()
    maven("https://github-package-registry-mirror.gc.nav.no/cached/maven-release")
}

val jacksonModuleKotlinVersion = "3.2.2"
val commonVersion = "4.2026.09.24_06.17-80dfc0eacb29"
val tokenValidationVersion = "6.0.12"
val okHttpVersion = "5.5.0"

dependencyManagement {
    dependencies {
        // Override Spring Boot's managed Jackson versions to apply the security fixes in 3.1.7.
        dependency("tools.jackson.core:jackson-core:3.1.7")
        dependency("tools.jackson.core:jackson-databind:3.1.7")
    }
}

dependencies {
    constraints {
        implementation("at.yawk.lz4:lz4-java") {
            version { strictly("1.11.2") }
            because("Fixes CVE-2026-59949")
        }
    }

    runtimeOnly("org.springframework.boot:spring-boot-starter-validation") // ikke fjern
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-web") {
        exclude(group = "org.springframework.boot", module = "spring-boot-starter-tomcat")
    }
    implementation("org.springframework.boot:spring-boot-starter-jetty")

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
