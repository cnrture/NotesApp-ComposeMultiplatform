import com.codingfeline.buildkonfig.compiler.FieldSpec
import java.util.Properties

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsCompose)
    alias(libs.plugins.mokoResources)
    alias(libs.plugins.buildKonfigPlugin)
    alias(libs.plugins.kotlinxSerialization)
}

kotlin {

    applyDefaultHierarchyTemplate()

    androidTarget {
        compilations.all {
            kotlinOptions {
                jvmTarget = "17"
            }
        }
    }

    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "ComposeApp"
            isStatic = true
        }
    }

    sourceSets {

        androidMain {
            dependencies {
                implementation(libs.compose.ui.tooling.preview)
                implementation(libs.androidx.activity.compose)
                implementation(libs.ktor.client.okhttp)
            }

            dependsOn(commonMain.get())
        }
        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material)
            implementation(compose.material3)
            implementation(compose.ui)
            implementation(compose.materialIconsExtended)

            implementation(libs.koin.core)
            implementation(libs.voyager.navigator)
            implementation(libs.voyager.bottomSheetNavigator)
            implementation(libs.voyager.koin)
            implementation(libs.moko.resources.compose)

            implementation(libs.co.touchlab.stately.common)

            implementation(libs.ktor.client.core)
            implementation(libs.ktor.client.content.negotiation)
            implementation(libs.ktor.serialization.kotlinx.json)

            implementation(libs.kamel)
        }

        iosMain.dependencies {
            implementation(libs.ktor.client.darwin)
        }
    }
}

android {
    namespace = "com.canerture.notes"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    sourceSets["main"].res.srcDirs("src/androidMain/res")
    sourceSets["main"].resources.srcDirs("src/commonMain/resources")

    defaultConfig {
        applicationId = "com.canerture.notes"
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.targetSdk.get().toInt()
        versionCode = 1
        versionName = "1.0"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    dependencies {
        debugImplementation(libs.compose.ui.tooling)
    }
}

buildkonfig {
    packageName = "com.canerture.notes"

    val localProperties = Properties().apply {
        val propsFile = rootProject.file("local.properties")
        if (propsFile.exists()) {
            load(propsFile.inputStream())
        }
    }

    defaultConfigs {
        buildConfigField(
            FieldSpec.Type.STRING,
            "BASE_URL",
            localProperties["base_url"]?.toString() ?: "",
        )
    }
}

multiplatformResources {
    multiplatformResourcesPackage = "com.canerture.notes"
}