plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("kotlin-kapt")
    id("kotlinx-serialization")
    id("kotlin-parcelize")
}

android {
    namespace = "com.brookes6.repository"
    compileSdk = 33

    defaultConfig {
        minSdk = 21
        targetSdk = 33

        kapt {
            arguments {
                arg("room.schemaLocation", "$projectDir/schemas")
            }
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {
    // kotlin-serialization
    api("org.jetbrains.kotlinx:kotlinx-serialization-json:1.4.1")
    // room
    api("androidx.room:room-runtime:2.4.2")
    kapt("androidx.room:room-compiler:2.4.2")
    api("androidx.room:room-ktx:2.4.2")
    //net
    api(project(mapOf("path" to ":net")))
    // 深拷贝 https://github.com/bennyhuo/KotlinDeepCopy
    api("com.bennyhuo.kotlin:deepcopy-reflect:1.7.10.0")
}