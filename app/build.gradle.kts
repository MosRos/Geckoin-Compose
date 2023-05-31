@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.parcelize)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.kapt)
    alias(libs.plugins.ksp)
    alias(libs.plugins.hilt)
}

android {
    compileSdk = libs.versions.compileSdk.get().toInt()
    namespace = "com.mrostami.geckoincompose"
    defaultConfig {
        applicationId = "com.mrostami.geckoincompose"
        minSdk = libs.versions.minSdk.get().toInt()
        targetSdk = libs.versions.targetSdk.get().toInt()
        versionCode = 1
        versionName = "1.0"
        vectorDrawables.useSupportLibrary = true
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        javaCompileOptions {
            annotationProcessorOptions {
                argument("includeCompileClasspath", "false")
            }
        }
        kapt {
            includeCompileClasspath = false
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

//    kotlinOptions {
//        jvmTarget = "1.8"
//    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.compose.compiler.get()
    }
    packaging {
        resources {
            excludes += "/META-INF/{DEPENDENCIES, LICENSE, LICENSE.txt, license.txt, NOTICE, NOTICE.txt, notice.txt, ASL2.0, LGPL2.1, AL2.0, *.kotlin_module}"
        }
        resources.excludes.add("META-INF/DEPENDENCIES")
        resources.excludes.add("META-INF/LICENSE")
        resources.excludes.add("META-INF/LICENSE.txt")
        resources.excludes.add("META-INF/license.txt")
        resources.excludes.add("META-INF/NOTICE")
        resources.excludes.add("META-INF/NOTICE.txt")
        resources.excludes.add("META-INF/notice.txt")
        resources.excludes.add("META-INF/ASL2.0")
        resources.excludes.add("META-INF/LGPL2.1")
        resources.excludes.add("META-INF/AL2.0")
        resources.excludes.add("META-INF/*.kotlin_module")
    }
}

dependencies {

    implementation(libs.kotlin.stdlib)

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.activity.ktx)
    implementation(libs.androidx.activity.compose)

    // Compose
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.foundation)
    implementation(libs.androidx.compose.foundation.layout)
    implementation(libs.androidx.compose.material)
    implementation(libs.androidx.compose.material.iconsExtended)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.ui.googlefonts)
    implementation(libs.androidx.compose.ui.tooling)
    implementation(libs.androidx.compose.ui.util)
    implementation(libs.androidx.constraintlayout.compose)
    implementation(libs.androidx.compose.animation)

    implementation(libs.accompanist.pager)
    implementation(libs.accompanist.pager.indicator)
    implementation(libs.accompanist.systemuicontroller)
    implementation(libs.accompanist.navigation.animation)
    implementation(libs.accompanist.navigation.material)

    implementation(libs.androidx.lifecycle.runtime.compose)

//    implementation(libs.google.android.material)
//    implementation(libs.androidx.glance)
//    implementation(libs.androidx.glance.appwidget)
//    implementation(libs.androidx.glance.material3)
    implementation(libs.androidx.window)


    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.savedstate)
    implementation(libs.androidx.lifecycle.livedata.ktx)
    implementation(libs.androidx.lifecycle.viewModelCompose)
    implementation(libs.androidx.lifecycle.runtime.compose)



    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.navigation.fragment)
    implementation(libs.androidx.navigation.ui.ktx)


//    implementation(libs)
    implementation(libs.preferences.ktx)
    implementation(libs.datastore)
    implementation(libs.datastore.preferences)
    // Security And DataStore
    implementation(libs.androidx.security)

    // *** Room ***
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    implementation(libs.androidx.room.paging)
    kapt(libs.androidx.room.compiler)
//    testImplementation("androidx.room:room-testing:${libs.versions.room.te}")

    // *** paging ***
    implementation(libs.androidx.paging.runtime)
    implementation(libs.androidx.paging.common)
    implementation(libs.androidx.paging.compose)


    // coroutines
    implementation(libs.coroutines.core)
    implementation(libs.coroutines.android)

    // arrow
    implementation(libs.arrow.core)

    // WorkManager
    implementation(libs.androidx.work.manager)
    implementation(libs.work.testing)
    // optional - Multiprocess support
    androidTestImplementation(libs.androidx.work.multiprocess)


    // Dagger Hilt
    implementation(libs.hilt.android)
    kapt(libs.hilt.compiler)
    implementation(libs.hilt.work)
    kapt(libs.hilt.ext.compiler)

    // Network & Json
    implementation(libs.okhttp3)
    implementation(libs.okhttp.logging)
    implementation(libs.gson)

    implementation(libs.ktor.core)
    implementation(libs.ktor.android)
    implementation(libs.ktor.okhttp)
    implementation(libs.ktor.logging)
    implementation(libs.ktor.serialization)
    implementation(libs.ktor.serialization.json)
    implementation(libs.ktor.content.negotiation)
//    implementation("com.github.haroldadmin:NetworkResponseAdapter:4.2.2")

    // Coil Image loading
    implementation(libs.coil.coil)
    implementation(libs.coil.kt.compose)

    // Charts
//    implementation("com.github.PhilJay:MPAndroidChart:v3.1.0")

    // Timber Logger
    implementation(libs.timber)

    implementation(libs.junit)
    implementation(libs.robolectric)
    implementation(libs.androidx.test.core)
    implementation(libs.androidx.test.runner)
    implementation(libs.androidx.test.ext.junit)
    implementation(libs.androidx.test.espresso.core)
}

tasks.withType<Test>().configureEach {
    systemProperties.put("robolectric.logging", "stdout")
}