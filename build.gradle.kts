plugins {
    kotlin("jvm") version "2.3.0"
    application
}

group = "com.sudoku"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
    testImplementation("org.junit.jupiter:junit-jupiter:5.10.2")
}

application {
    mainClass.set("sudoku.MainKt")
}

kotlin {
    jvmToolchain(25)
}

tasks.test {
    useJUnitPlatform()
}