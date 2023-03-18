plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("kotlin-android-extensions")
}

@Suppress("UnstableApiUsage")
android {
    compileSdk = 33

    defaultConfig {
        minSdk = 21
        targetSdk = 33
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = "1.8"
    }

    lint.abortOnError = false
    androidExtensions.isExperimental = false

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

}

val exoplayer: String = "2.15.0"

dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
    // repository
    api(project(mapOf("path" to ":repository")))
    //noinspection GradleCompatible
    implementation("androidx.appcompat:appcompat:1.2.0")
    api("androidx.work:work-runtime-ktx:2.7.1")
    api("com.google.android.exoplayer:exoplayer-core:$exoplayer")
    api("com.google.android.exoplayer:extension-mediasession:$exoplayer")
    compileOnly("com.google.android.exoplayer:exoplayer-dash:$exoplayer")
    compileOnly("com.google.android.exoplayer:exoplayer-hls:$exoplayer")
    compileOnly("com.google.android.exoplayer:exoplayer-smoothstreaming:$exoplayer")
    compileOnly("com.google.android.exoplayer:extension-rtmp:$exoplayer")
    compileOnly("com.google.android.exoplayer:exoplayer-rtsp:$exoplayer")
    implementation("org.jetbrains.kotlin:kotlin-stdlib:1.7.10")
}