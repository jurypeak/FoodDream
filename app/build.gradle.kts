import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
}

android {
    namespace = "com.example.fooddream"
    compileSdk = 35

    buildFeatures {
        buildConfig = true  // Enable BuildConfig fields generation
    }

    buildFeatures {
        buildConfig = true  // Enable BuildConfig fields generation
    }

    defaultConfig {
        // Define a localProperties variable to load the local.properties file
        val localProperties = Properties()  // Initialize Properties properly
        val localPropertiesFile = rootProject.file("local.properties")

        // Load the properties from the local.properties file
        if (localPropertiesFile.exists()) {
            localProperties.load(localPropertiesFile.inputStream())
        }

        // Retrieve the values from local.properties
        val loginURL = localProperties.getProperty("URL_LOGIN", "defaultLoginURL")
        val domain = localProperties.getProperty("DOMAIN", "defaultDomain")
        val registerURL = localProperties.getProperty("URL_REGISTER", "defaultRegisterURL")

        // Inject these values into BuildConfig
        buildConfigField("String", "URL_LOGIN", "\"$loginURL\"")
        buildConfigField("String", "DOMAIN", "\"$domain\"")
        buildConfigField("String", "URL_REGISTER", "\"$registerURL\"")

        applicationId = "com.example.fooddream"
        minSdk = 24
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
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.jbcrypt)
    implementation(libs.volley)
    implementation(libs.json)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}