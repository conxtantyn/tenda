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
            baseName = "TendaCoreUi"
            isStatic = true
        }
    }
    sourceSets {
        androidMain.dependencies {
            api(compose.preview)
            api(libs.androidx.activity.compose)
        }
        commonMain.dependencies {
            implementation(project(":core:common"))

            api(compose.runtime)
            api(compose.foundation)
            api(compose.material3)
            api(compose.ui)
            api(compose.components.resources)
            api(compose.components.uiToolingPreview)

            api(libs.koin.compose)
            api(libs.androidx.lifecycle.viewmodelCompose)
            api(libs.androidx.lifecycle.runtimeCompose)

            api(libs.voyager)
            api(libs.voyager.model)
            api(libs.voyager.transitions)
            api(libs.voyager.koin)
        }
    }
}

dependencies {
    debugApi(compose.uiTooling)
}
