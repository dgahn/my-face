import io.gitlab.arturbosch.detekt.Detekt

plugins {
    kotlin("jvm") version "1.6.10"
    kotlin("plugin.spring") version "1.6.10"
    kotlin("plugin.jpa") version "1.6.10"
    kotlin("plugin.allopen") version "1.6.10"
    kotlin("plugin.serialization") version "1.6.10"
    id("org.springframework.boot") version "2.7.5"
    id("io.spring.dependency-management") version "1.1.0"
    id("io.gitlab.arturbosch.detekt") version "1.18.0"
    id("org.jmailen.kotlinter") version "3.6.0"
}

group = "me.dgahn"
version = "0.1.0"

repositories {
    google()
    mavenCentral()
}

allOpen {
    annotation("javax.persistence.Entity")
    annotation("javax.persistence.Embeddable")
    annotation("javax.persistence.MappedSuperclass")
}

noArg {
    annotation("javax.persistence.Entity")
    annotation("javax.persistence.MappedSuperclass")
    annotation("javax.persistence.Embeddable")
    annotation("kotlinx.serialization.Serializable")
}

kotlinter {
    ignoreFailures = false
    indentSize = 4
    reporters = arrayOf("checkstyle", "plain")
    experimentalRules = false
    disabledRules = emptyArray()
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("io.jsonwebtoken:jjwt-api:0.11.5")
    implementation("io.jsonwebtoken:jjwt-impl:0.11.5")
    implementation("io.jsonwebtoken:jjwt-jackson:0.11.5")
    implementation("io.github.microutils:kotlin-logging-jvm:3.0.2")
    implementation("mysql:mysql-connector-java:8.0.31")
    runtimeOnly("org.jetbrains.kotlin:kotlin-reflect:1.6.21")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
}

tasks {
    java {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    compileKotlin {
        kotlinOptions {
            jvmTarget = "11"
        }
        dependsOn(detekt)
    }
    compileTestKotlin {
        kotlinOptions {
            jvmTarget = "11"
        }
    }
    withType<Detekt> {
        dependsOn(formatKotlin)
    }
    bootJar {
        archiveFileName.set("api-server.jar")
        doLast {
            copy {
                from("build/libs/api-server.jar")
                into("docker/server")
            }
        }
    }
}
