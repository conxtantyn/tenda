plugins {
    alias(libs.plugins.cargo)
    alias(libs.plugins.uniffi)
    kotlin("plugin.atomicfu") version libs.versions.kotlin.get()
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
            baseName = "TendaPersistenceDatabase"
            isStatic = true
        }
    }
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(project(":core:common"))
                implementation(project(":core:persistence"))
                kotlin.srcDir("build/generated/uniffi/commonMain/kotlin")
            }
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
            implementation(libs.kotlinx.test.coroutine)
        }
    }
}

configurations {
    findByName("uniFfiConfiguration")?.let { remove(it) }
    create("uniFfiConfiguration") {
        isCanBeResolved = true
        isCanBeConsumed = false
    }
    matching { it.name.contains("RustRuntime") || it.name.contains("minGWX64") }
        .all {
            exclude(group = "Tenda.core", module = "common")
            exclude(group = "Tenda.core", module = "persistence")
        }
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
    dependsOn("buildUniffiBindings")
}

tasks.matching { it.name.startsWith("ksp") }.configureEach {
    dependsOn("buildUniffiBindings")
}
