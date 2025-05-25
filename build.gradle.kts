plugins {
    kotlin("jvm") version "1.8.0"
    application
}

repositories {
    mavenCentral()
    maven {
        url = uri("https://maven.pkg.github.com/peoplelogic/directory")
        credentials {
            username = System.getenv("GITHUB_USERNAME") ?: "<GITHUB_USERNAME>"
            password = System.getenv("GITHUB_TOKEN") ?: "<GITHUB_TOKEN>"
        }
    }
}

dependencies {
    implementation("ai.peoplelogic:directory-client:1.5.0")
    implementation("ai.peoplelogic:okrs-client:1.5.0")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.1")
}

application {
    mainClass.set("MainKt")
}
