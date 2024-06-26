buildscript {
    dependencies {
        classpath("crackers.buildstuff:crackers-gradle-plugins:1.0.1")
    }
}

plugins {
    kotlin("jvm") version "1.9.10"
    `java-library`
    idea
    id("org.jmailen.kotlinter") version "3.12.0"
    id("library-publish") version "1.0.1"
    id("org.jetbrains.dokka") version "1.8.10"
    id("net.thauvin.erik.gradle.semver") version "1.0.4"
}

repositories {
    mavenCentral()
    mavenLocal()
}

dependencies {
    api("org.json:json:20231013")
    api("org.slf4j:slf4j-api:2.0.0")

    testImplementation("com.typesafe:config:1.4.2")
    testImplementation("io.kotest:kotest-runner-junit5:5.5.4")
    testImplementation("io.mockk:mockk:1.13.3")
    testImplementation("ch.qos.logback:logback-classic:1.4.12")
}

kotlin {
    jvmToolchain(17)
}

kotlinter {
    // ignore failures because the build re-formats it
    ignoreFailures = true
    disabledRules = arrayOf("no-wildcard-imports")
}

group = "crackers.automation"

tasks {
    build {
        dependsOn("formatKotlin")
    }
    check {
//        dependsOn("installKotlinterPrePushHook")
        dependsOn("formatKotlin")
    }
    test {
        useJUnitPlatform()
    }
    // make docs
    dokkaJavadoc {
        mustRunAfter("javadoc")
        outputDirectory.set(file("$projectDir/build/docs"))
    }
    javadocJar {
        mustRunAfter("dokkaJavadoc")
        include("$projectDir/build/docs")
    }
    // jar docs
    register<Jar>("dokkaJavadocJar") {
        dependsOn(dokkaJavadoc)
        from(dokkaJavadoc.flatMap { it.outputDirectory })
        archiveClassifier.set("javadoc")
    }
    generateMetadataFileForLibraryPublication {
        mustRunAfter("dokkaJavadocJar")
    }
}

defaultTasks("clean", "build", "dokkaJavadocJar", "libraryDistribution")
