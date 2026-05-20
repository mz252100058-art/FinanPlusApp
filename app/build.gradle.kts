import org.gradle.kotlin.dsl.implementation

plugins {
    alias(libs.plugins.android.application)
    id("com.google.gms.google-services")
}

android {

    namespace = "com.example.FinanPlus"

    compileSdk = 36

    defaultConfig {

        applicationId = "com.example.FinanPlus"

        minSdk = 24

        targetSdk = 36

        versionCode = 1

        versionName = "1.0"

        testInstrumentationRunner =
            "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {

        release {

            isMinifyEnabled = false

            proguardFiles(
                getDefaultProguardFile(
                    "proguard-android-optimize.txt"
                ),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {

        sourceCompatibility = JavaVersion.VERSION_11

        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {

    // Android básicas
    implementation(libs.androidx.core.ktx)

    implementation(libs.androidx.appcompat)

    implementation(libs.androidx.activity)

    implementation(libs.androidx.constraintlayout)

    // Material Design
    implementation("com.google.android.material:material:1.11.0")

    // RecyclerView
    implementation("androidx.recyclerview:recyclerview:1.3.2")

    // CardView
    implementation("androidx.cardview:cardview:1.0.0")

    // 🔥 Firebase BoM
    implementation(
        platform(
            "com.google.firebase:firebase-bom:33.0.0"
        )
    )

    // 🔐 Firebase Authentication
    implementation("com.google.firebase:firebase-auth")

    // ☁️ Firebase Firestore
    implementation("com.google.firebase:firebase-firestore")
    implementation(libs.material)

    // Testing
    testImplementation(libs.junit)

    androidTestImplementation(libs.androidx.junit)

    androidTestImplementation(libs.androidx.espresso.core)
}