plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "vn.edu.usth.mcma"
    compileSdk = 35

    defaultConfig {
        applicationId = "vn.edu.usth.mcma"
        minSdk = 31
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    dataBinding {
        enable = true
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
    buildFeatures {
        viewBinding = true
    }
}

dependencies {

    // Thư viện Flexbox
    implementation("com.google.android.flexbox:flexbox:3.0.0")

    // Thư viện RecyclerView, CardView, Glide
    implementation("androidx.recyclerview:recyclerview:1.3.2")
    implementation("androidx.cardview:cardview:1.0.0")
    implementation("com.github.bumptech.glide:glide:4.16.0")
    annotationProcessor("com.github.bumptech.glide:compiler:4.16.0")

    // Thư viện Material Design
    implementation("com.google.android.material:material:1.12.0")

    // Thư viện khác
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)

    // Kiểm thử
    testImplementation(libs.junit)

    // Retrofit and OkHttp
    implementation("com.squareup.retrofit2:retrofit:2.11.0")
    implementation("com.squareup.retrofit2:converter-gson:2.11.0")
    implementation("com.squareup.retrofit2:converter-scalars:2.11.0")
    implementation("com.squareup.okhttp3:okhttp:4.12.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.12.0")

    // Glide
    implementation("com.github.bumptech.glide:glide:4.16.0")
    annotationProcessor("com.github.bumptech.glide:compiler:4.16.0")
    implementation(libs.navigation.fragment)
    implementation(libs.navigation.ui)

    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)

    //helper
    compileOnly(libs.projectlombok.lombok)
    annotationProcessor(libs.projectlombok.lombok)

    implementation(libs.swiperefreshlayout)

    implementation("com.google.android.gms:play-services-maps:18.1.0")
    implementation("com.google.android.gms:play-services-location:21.0.1")

    implementation("com.google.android.exoplayer:exoplayer:2.18.1")
}
java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(17)
    }
}
