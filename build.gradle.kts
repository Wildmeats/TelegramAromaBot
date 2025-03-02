plugins {
    kotlin("jvm") version "2.0.20"
    application
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
    implementation("io.github.cdimascio:dotenv-kotlin:6.4.1")
    implementation("org.jetbrains.kotlin:kotlin-stdlib")
    implementation("org.telegram:telegrambots:6.5.0")
    implementation("org.slf4j:slf4j-api:2.0.9") // SLF4J API
    implementation("org.slf4j:slf4j-simple:2.0.9") // SLF4J Simple Binding
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(17)
}

tasks.withType<Jar> {
    manifest {
        attributes["Main-Class"] = "org.example.MainKt"
    }
    from(sourceSets.main.get().output)
}