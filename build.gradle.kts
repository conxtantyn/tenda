plugins {
    // this is necessary to avoid the plugins to be loaded multiple times
    // in each subproject's classloader
    alias(libs.plugins.androidApplication) apply false
    alias(libs.plugins.androidLibrary) apply false
    alias(libs.plugins.composeMultiplatform) apply false
    alias(libs.plugins.composeCompiler) apply false
    alias(libs.plugins.kotlinMultiplatform) apply false
    alias(libs.plugins.ksp) apply false
    alias(libs.plugins.cargo) apply false
    alias(libs.plugins.uniffi) apply false
}

subprojects {
    ext.set("android", listOf(":app"))
    ext.set("composable", listOf(":core:ui"))
    beforeEvaluate {
        project(path) {
            apply("$rootDir/gradle/common.gradle")
        }
    }
}
