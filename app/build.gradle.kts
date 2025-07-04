plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.google.devtool.ksp)
    alias(libs.plugins.hilt)
}

android {
    namespace = "com.example.showingvideos"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.showingvideos"
        minSdk = 28
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        buildConfigField("String", "PEXELS_API_KEY", "\"${project.property("PEXELS_API_KEY")}\"")
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
}

dependencies {

    //flipper
    debugImplementation(libs.flipper)
    debugImplementation(libs.flipper.soloader)
    releaseImplementation(libs.flipper.noop)
    debugImplementation(libs.flipper.network.plugin)

    // androidx
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)


    // API call
    implementation(libs.moshi)
    ksp(libs.moshi.ksp)
    implementation(libs.retrofit)
    implementation(libs.retrofit.converter.moshi)

    // hilt
    implementation(libs.hilt.android)
    ksp(libs.hilt.android.compiler)

    // exo player
    implementation(libs.media3.ui)
    implementation(libs.media3.exoplayer)
    implementation(libs.media3.exoplayer.dash)
    implementation(libs.media3.ui.compose)

    implementation(libs.coil.compose)
    implementation(libs.coil.network)


    //test
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}