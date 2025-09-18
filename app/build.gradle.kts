plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.google.gms.google.services)
}

android {
    namespace = "com.example.ventaexpress"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.example.ventaexpress"
        minSdk = 24
        targetSdk = 36
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
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.firebase.auth)
    implementation(libs.play.services.analytics.impl)
    implementation(libs.firebase.database.ktx)
    implementation(libs.firebase.database)
    implementation("com.google.firebase:firebase-auth-ktx:24.0.1")
    implementation("com.google.firebase:firebase-database-ktx:20.0.0")
    implementation("com.google.android.gms:play-services-auth:20.6.0")
    implementation("com.facebook.android:facebook-login:latest_version")
    implementation ("com.google.firebase:firebase-firestore-ktx:24.0.0")
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}