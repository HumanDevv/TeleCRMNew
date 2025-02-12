plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    id("kotlin-kapt")
    alias(libs.plugins.serialization)
    alias(libs.plugins.hiltDagger)
}

android {
    namespace = "com.tele.crm"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.tele.crm"
        minSdk = 24
        targetSdk = 34
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
        viewBinding= true
        buildConfig= true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.navigation.ui.ktx)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    //navigation
    implementation(libs.navigation.fragment)
    implementation(libs.navigation.ui)
    //hilt
    implementation(libs.dagger.hilt)
    kapt(libs.dagger.hilt.compiler)

    //retrofit
    implementation(libs.retrofit.retrofit)
    implementation(libs.retrofit.converter)
    implementation(libs.okhttp.okhttp)
    implementation(libs.okhttp.logging)

    //To change dimensions according to screen size
    implementation(libs.sdp.sdp)
    implementation(libs.ssp.ssp)

    kapt(libs.dagger.hilt.compiler)
    implementation(libs.dagger.hilt)

    //coroutines
    implementation(libs.coroutines)

    // kotlin-serialization
    implementation(libs.kotlinx.serialization.core)
    implementation(libs.kotlinx.serialization.json)

    //datastore
    implementation(libs.datastore)

    //Glide
    implementation(libs.glide)
    implementation ("androidx.viewpager2:viewpager2:1.1.0")

    implementation("ir.mahozad.android:pie-chart:0.7.0")

}