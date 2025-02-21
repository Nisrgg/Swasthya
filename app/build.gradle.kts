plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.google.gms.google.services)
}

android {
    namespace = "com.example.hospital"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.hospital"
        minSdk = 25
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.firebase.auth)
    implementation(libs.play.services.auth)

    implementation(libs.gson)
    implementation(libs.volley)
    implementation(libs.generativeai)
    implementation(libs.firebase.database)
    implementation(libs.androidx.recyclerview)
    implementation(libs.material)
    implementation(libs.androidx.compose.material)
    implementation(libs.firebase.firestore.ktx)
    implementation(libs.androidx.datastore.core.android)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    implementation(libs.generativeai.v040)

    implementation(libs.firebase.ui.database)  // Use latest version

    implementation (libs.glide)
    implementation(libs.coil.kt.coil.compose)
    implementation(libs.firebase.ui.database.v621)
    implementation(libs.firebase.database.v1931)
    implementation(libs.firebase.storage)
    implementation(libs.retrofit)
    implementation(libs.converter.gson)

    implementation(libs.material3)
    implementation(libs.ui.tooling.preview)
    implementation(libs.accompanist.pager) // For Calendar


}