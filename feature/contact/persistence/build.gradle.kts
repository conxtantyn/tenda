plugins {
    kotlin("plugin.serialization") version libs.versions.serialization.get()
}

kotlin {
    androidTarget()

    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "TendaContactPersistenceFeature"
            isStatic = true
        }
    }
    sourceSets {
        commonMain.dependencies {
            implementation(project(":core:common"))
            implementation(project(":core:persistence"))

            implementation(project(":feature:contact:domain"))
            implementation(project(":feature:contact:data"))

            implementation(libs.koin.core)
            implementation(libs.koin.annotation)
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
            implementation(libs.kotlinx.test.coroutine)
        }
    }
}
