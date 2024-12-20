plugins {
//    id("java")
    // id("org.jetbrains.intellij") version "1.6.0"
//    id("org.jetbrains.intellij.platform") version "2.2.0"

    id("java")
    id("org.jetbrains.kotlin.jvm") version "1.9.25"
    id("org.jetbrains.intellij") version "1.17.4"
}

group = "semicontinuity.idea.code.analyzer.golang"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    // intellijPlatform {
    //     defaultRepositories()
    // }
}

// Configure Gradle IntelliJ Plugin
// Read more: https://plugins.jetbrains.com/docs/intellij/tools-gradle-intellij-plugin.html
intellij {
    // version.set("2021.1.1")
    version.set("2023.2.6")
    type.set("GO") // Target IDE Platform

    plugins.set(listOf("org.jetbrains.plugins.go"))
}

dependencies {
    implementation("com.fasterxml.jackson.core:jackson-annotations:2.13.2")
    implementation("com.fasterxml.jackson.core:jackson-core:2.13.2")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.13.2")
}

dependencies {
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.1")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.8.1")
}

tasks {
    // Set the JVM compatibility versions
    withType<JavaCompile> {
        sourceCompatibility = "17"
        targetCompatibility = "17"
    }
    withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
        kotlinOptions.jvmTarget = "17"
    }
    
    patchPluginXml {
        sinceBuild.set("232")
        untilBuild.set("242.*")
    }

//    patchPluginXml {
        // sinceBuild.set("211")
//        sinceBuild.set("242")
        // untilBuild.set("223.*")
//        untilBuild.set("242.*")
//    }

    signPlugin {
        certificateChain.set(System.getenv("CERTIFICATE_CHAIN"))
        privateKey.set(System.getenv("PRIVATE_KEY"))
        password.set(System.getenv("PRIVATE_KEY_PASSWORD"))
    }

    publishPlugin {
        token.set(System.getenv("PUBLISH_TOKEN"))
    }

    test {
        useJUnitPlatform {
        }
    }
}

