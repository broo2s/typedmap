import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.5.0"
    id("org.jetbrains.dokka") version "1.4.32"
}

group = "me.broot.typedmap"
version = "1.0.0-SNAPSHOT"

subprojects {
    apply(plugin = "org.jetbrains.kotlin.jvm")
    apply(plugin = "org.jetbrains.dokka")

    tasks.test {
        useTestNG()
    }

    tasks.withType<KotlinCompile> {
        kotlinOptions {
            jvmTarget = "11"
            freeCompilerArgs = freeCompilerArgs + "-Xopt-in=kotlin.RequiresOptIn"
        }
    }

    dependencies {
        testImplementation(kotlin("test-testng"))
    }
}

allprojects {
    repositories {
        mavenCentral()
    }
}
