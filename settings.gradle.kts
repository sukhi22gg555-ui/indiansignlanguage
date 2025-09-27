// In settings.gradle.kts

pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google() // <-- THIS LINE IS CRITICAL. MAKE SURE IT'S HERE.
        mavenCentral()
    }
}
rootProject.name = "IndianSignLanguage"
include(":app")