pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven { url = uri("https://jitpack.io") }
    }
}

rootProject.name = "NETI Client"

include(":app")
include(":app_wearable")

include(":core:data")
include(":core:design")
include(":core:navigation")

include(":feature:auth")
include(":feature:profile")
include(":feature:contact_info")
include(":feature:home")
include(":feature:news_detail")
include(":feature:search_group")
include(":feature:calendar")
include(":feature:image_viewer")
include(":feature:lesson_detail")
include(":feature:sessia_results")
include(":feature:share_score")
include(":feature:money")
include(":feature:messages")
include(":feature:messages_detail")
include(":feature:swap_lks")
include(":feature:search_person")
include(":feature:person_detail")
include(":feature:person_gallery")
include(":feature:docs")
include(":feature:doc_detail")
include(":feature:new_document")
include(":feature:settings")
include(":feature:settings_account")
include(":feature:settings_labs")
include(":feature:settings_theme")
include(":feature:settings_notifications")
include(":feature:settings_about")