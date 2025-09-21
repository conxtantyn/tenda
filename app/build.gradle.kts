import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

android {
    defaultConfig {
        versionCode = 1
        versionName = "1.0"
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
}
kotlin {
    androidTarget {
        @OptIn(ExperimentalKotlinGradlePluginApi::class)
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }
    }
    
    listOf(
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "TendaApp"
            isStatic = true
        }
    }
    
    sourceSets {
        androidMain.dependencies {
            implementation(compose.preview)
            implementation(libs.androidx.activity.compose)
            implementation(libs.androidx.splash)
        }
        commonMain.dependencies {
            implementation(project(":core:common"))
            implementation(project(":core:persistence"))
            implementation(project(":core:ui"))

            implementation(project(":database"))

            implementation(project(":feature:contact:domain"))
            implementation(project(":feature:contact:data"))
            implementation(project(":feature:contact:persistence"))
            implementation(project(":feature:contact:ui"))

            implementation(compose.components.resources)

            implementation(libs.koin.core)
            implementation(libs.koin.annotation)
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
    }
}
dependencies {
    debugImplementation(compose.uiTooling)
}

