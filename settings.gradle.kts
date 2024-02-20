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
                username =  System.getenv("USERNAME_GITHUB")
                password =  System.getenv("TOKEN_GITHUB")
            }
        }

    }
}

rootProject.name = "Movies"
include(":app")
 