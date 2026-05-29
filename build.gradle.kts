import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.compose) apply false
    alias(libs.plugins.jetbrains.kotlin.jvm) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.kover) apply true
}

subprojects {
    pluginManager.apply("org.jetbrains.kotlinx.kover")
}

dependencies {
    kover(project(":app"))
    kover(project(":core:common"))
    kover(project(":core:model"))
    kover(project(":core:network"))
    kover(project(":core:designsystem"))
    kover(project(":feature:splash"))
    kover(project(":feature:home"))
    kover(project(":feature:settings"))
}

allprojects {
    tasks.withType<KotlinCompile>().configureEach {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }
    }
}