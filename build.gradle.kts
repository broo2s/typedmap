import com.palantir.gradle.gitversion.VersionDetails
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.5.0"
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

    dependencies {
        testImplementation(kotlin("test-testng"))
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
