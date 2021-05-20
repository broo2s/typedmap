import com.palantir.gradle.gitversion.VersionDetails
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.5.0"
    `maven-publish`
    signing

    id("org.jetbrains.dokka") version "1.4.32" apply false
    id("com.palantir.git-version") version "0.12.3" apply false
}

try {
    apply(plugin = "com.palantir.git-version")
} catch (e: Exception) {
    project.logger.warn(e.message, e)
}

val projectVersion = createProjectVersion()

allprojects {
    group = "me.broot.typedmap"
    version = projectVersion.name

    repositories {
        mavenCentral()
    }
}

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

    val dokkaHtml by tasks.getting(org.jetbrains.dokka.gradle.DokkaTask::class)
    val dokkaJavadoc by tasks.getting(org.jetbrains.dokka.gradle.DokkaTask::class)

    val dokkaJar by tasks.registering(Jar::class) {
        dependsOn(dokkaHtml)
        archiveClassifier.set("dokka")
        from(dokkaHtml.outputDirectory)
    }

    val javadocJar by tasks.registering(Jar::class) {
        dependsOn(dokkaJavadoc)
        archiveClassifier.set("javadoc")
        from(dokkaJavadoc.outputDirectory)
    }

    dependencies {
        testImplementation(kotlin("test-testng"))
    }

    if (name in listOf("typedmap-core")) {
        apply(plugin = "maven-publish")
        apply(plugin = "signing")

        java {
            withSourcesJar()
        }

        kotlin {
            explicitApi()
        }

        publishing {
            publications {
                create<MavenPublication>("mavenCentral") {
                    version = if (projectVersion.isRelease) projectVersion.name else "${projectVersion.name}-SNAPSHOT"

                    from(components["java"])
                    artifact(javadocJar)
                    artifact(dokkaJar)

                    pom {
                        name.set("typedmap")
                        description.set("Type-safe heterogeneous map in Kotlin.")
                        url.set("https://github.com/brutall/typedmap")

                        licenses {
                            license {
                                name.set("The Apache License, Version 2.0")
                                url.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
                            }
                        }
                        developers {
                            developer {
                                name.set("Ryszard Wiśniewski")
                                email.set("brut.alll@gmail.com")
                            }
                        }
                        scm {
                            connection.set("scm:git:git://github.com/brutall/typedmap.git")
                            developerConnection.set("scm:git:ssh://git@github.com/brutall/typedmap.git")
                            url.set("https://github.com/brutall/typedmap")
                        }
                    }
                }
            }

            repositories {
                maven {
                    name = "ossrh"

                    if (projectVersion.isRelease) {
                        setUrl("https://s01.oss.sonatype.org/service/local/staging/deploy/maven2/")
                    } else {
                        setUrl("https://s01.oss.sonatype.org/content/repositories/snapshots")
                    }

                    val ossrhUsername: String? by project
                    val ossrhPassword: String? by project
                    if (ossrhUsername != null && ossrhPassword != null) {
                        credentials {
                            username = ossrhUsername
                            password = ossrhPassword
                        }
                    }
                }
            }
        }

        signing {
            useGpgCmd()
            sign(publishing.publications["mavenCentral"])
        }
    }
}

fun Project.createProjectVersion() : ProjectVersion {
    if (!extra.has("versionDetails")) {
        return ProjectVersion("unknown", false, null)
    }

    val versionDetails: groovy.lang.Closure<VersionDetails> by extra
    val details = versionDetails(mapOf("prefix" to "release/"))

    val name = buildString {
        append(details.lastTag)

        if (details.commitDistance != 0) {
            details.branchName?.let {
                append('-')
                append(it.replace('/', '-'))
            }

            append('-')
            append(details.gitHash)
        }

        if (!details.isCleanTag) {
            append("-dirty")
        }
    }

    return ProjectVersion(
        name,
        details.commitDistance == 0 && details.isCleanTag,
        details
    )
}

data class ProjectVersion(
    val name: String,
    val isRelease: Boolean,
    val details: VersionDetails?
)
