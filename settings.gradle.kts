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
        google()
        mavenCentral()
        maven {
            url = uri("https://maven.pkg.github.com/Teut2711/TMBDMovies")
            credentials {
                username =  "Teut2711" //System.getenv("USERNAME_GITHUB")
                password =  "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" //System.getenv("TOKEN_GITHUB")
            }
        }

    }
}

rootProject.name = "Movies"
include(":app")
 