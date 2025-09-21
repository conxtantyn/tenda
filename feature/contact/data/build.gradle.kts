kotlin {
    androidTarget()

    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "TendaContactDataFeature"
            isStatic = true
        }
    }
    sourceSets {
        commonMain.dependencies {
            implementation(project(":core:common"))
            implementation(project(":feature:contact:domain"))

            implementation(libs.koin.core)
            implementation(libs.koin.annotation)
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
            implementation(libs.kotlinx.test.coroutine)
        }
    }
}
