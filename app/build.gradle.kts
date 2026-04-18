plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.example.photobooth"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.photobooth"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}

dependencies {
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.constraintlayout)
    implementation(libs.recyclerview)
    
    implementation(libs.glide)
    annotationProcessor(libs.compiler)
    
    implementation(libs.room.runtime)
    annotationProcessor(libs.room.compiler)
}
