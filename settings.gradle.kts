import java.net.URL

pluginManagement {
    repositories {
        gradlePluginPortal()
        mavenCentral()
        maven("https://kotlin.bintray.com/kotlinx")
        maven("https://jitpack.io")
        google()
    }
}
@Suppress("UnstableApiUsage")
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        maven(URL("https://maven.aliyun.com/repository/google"))
        maven(URL("https://maven.aliyun.com/repository/public"))
        maven(URL("https://maven.aliyun.com/repository/central"))
        maven(URL("https://maven.aliyun.com/nexus/content/repositories/releases/"))
        maven(URL("https://jitpack.io"))
        mavenCentral()
        google()
    }
}
rootProject.name = "CloudMusic"
include(":app")
include(":net")
include(":repository")
include(":starrysky")
