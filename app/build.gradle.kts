plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("kotlin-kapt")
}

@Suppress("UnstableApiUsage")
android {
    namespace = "com.brookes6.cloudmusic"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.brookes6.cloudmusic"
        minSdk = 21
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    signingConfigs {
        getByName("debug") {
            storePassword = "121300"
            keyAlias = "cloud"
            keyPassword = "121300"
            storeFile = file("../Test.jks")
        }
        create("release") {
            storePassword = "121300"
            keyAlias = "cloud"
            keyPassword = "121300"
            storeFile = file("../Test.jks")
        }
    }

    buildTypes {
        getByName("debug") {
            isMinifyEnabled = false
            isShrinkResources = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
        getByName("release") {
            isMinifyEnabled = true
            isShrinkResources = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            signingConfig = signingConfigs.getByName("release")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    kotlinOptions {
        jvmTarget = "11"
    }

    buildFeatures.compose = true
    buildFeatures.dataBinding = true

    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.4"
    }
    packagingOptions {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

val composeUiVersion by extra("1.5.4")

dependencies {
    implementation("androidx.core:core-ktx:1.9.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.5.1")
    // Android view
    implementation("androidx.recyclerview:recyclerview:1.3.0")
    implementation("androidx.fragment:fragment:1.5.5")
    implementation("com.google.android.material:material:1.5.0")
    implementation("androidx.appcompat:appcompat:1.0.0")
    // Test
    androidTestImplementation("androidx.test.ext:junit:1.1.3")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.4.0")
    androidTestImplementation("androidx.compose.ui:ui-test-junit4:$composeUiVersion")
    debugImplementation("androidx.compose.ui:ui-tooling:$composeUiVersion")
    debugImplementation("androidx.compose.ui:ui-test-manifest:$composeUiVersion")
    // Compose相关
    // 约束布局
    implementation("androidx.constraintlayout:constraintlayout-compose:1.0.1")
    // navigation
    implementation("androidx.navigation:navigation-compose:2.7.6")
    // viewModel
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.6.1")
    implementation("androidx.compose.runtime:runtime-livedata:${composeUiVersion}")
    implementation("androidx.activity:activity-compose:1.8.1")
    implementation("androidx.compose.ui:ui:$composeUiVersion")
    implementation("androidx.compose.ui:ui-tooling-preview:$composeUiVersion")
    implementation("androidx.compose.material:material:${composeUiVersion}")
    // StarrySky https://github.com/EspoirX/StarrySky
    implementation(project(mapOf("path" to ":starrysky")))

    // 第三方
    // Coil https://github.com/coil-kt/coil
    implementation("io.coil-kt:coil-compose:2.5.0")
    // BRV https://github.com/liangjingkanji/BRV
    implementation("com.github.liangjingkanji:BRV:1.5.6")
    // Android 高性能读写本地数据 https://github.com/liangjingkanji/Serialize
    implementation("com.github.liangjingkanji:Serialize:1.3.2")
    // 状态栏 https://github.com/google/accompanist
    implementation("com.google.accompanist:accompanist-insets:0.28.0")
    implementation("com.google.accompanist:accompanist-systemuicontroller:0.28.0")
    // Lottie http://airbnb.io/lottie/#/android-compose
    implementation("com.airbnb.android:lottie:6.1.0")
    // GSYVideoPlayer https://github.com/CarGuo/GSYVideoPlayer
    implementation("com.github.CarGuo.GSYVideoPlayer:gsyVideoPlayer-java:v8.5.0-release-jitpack")
    implementation("com.github.CarGuo.GSYVideoPlayer:gsyVideoPlayer-arm64:v8.5.0-release-jitpack")
    // Compose自定义View https://github.com/ltttttttttttt/ComposeViews
    implementation("io.github.ltttttttttttt:ComposeViews:1.4.0.2")
}
