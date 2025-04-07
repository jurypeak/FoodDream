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

    defaultConfig {
        // Define a localProperties variable to load the local.properties file
        var localProperties = Properties()  // Initialize Properties properly
        var localPropertiesFile = rootProject.file("local.properties")

        // Load the properties from the local.properties file
        if (localPropertiesFile.exists()) {
            localProperties.load(localPropertiesFile.inputStream())
        }

        // Retrieve the values from local.properties
        var loginURL = localProperties.getProperty("URL_LOGIN", "defaultLoginURL")
        var domain = localProperties.getProperty("DOMAIN", "defaultDomain")
        var registerURL = localProperties.getProperty("URL_REGISTER", "defaultRegisterURL")
        var userGuideURL = localProperties.getProperty("URL_USERGUIDE", "defaultUserGuideURL")
        var verifyEmailURL = localProperties.getProperty("URL_VERIFY_EMAIL", "defaultVerifyEmailURL")
        var productsURL = localProperties.getProperty("URL_PRODUCTS", "defaultProductURL")
        var resetPasswordURL = localProperties.getProperty("URL_RESETPASSWORD", "defaultResetPasswordURL")
        var orderURL = localProperties.getProperty("URL_ORDER", "defaultOrderURL")
        var addressURL = localProperties.getProperty("URL_ADDRESS", "defaultAddressURL")
        var paymentURL = localProperties.getProperty("URL_PAYMENT", "defaultPaymentURL")
        var orderItemsURL = localProperties.getProperty("URL_ORDER_ITEMS", "defaultOrderItemsURL")

        // Inject these values into BuildConfig
        buildConfigField("String", "URL_LOGIN", "\"$loginURL\"")
        buildConfigField("String", "DOMAIN", "\"$domain\"")
        buildConfigField("String", "URL_REGISTER", "\"$registerURL\"")
        buildConfigField("String", "URL_VERIFY_EMAIL", "\"$verifyEmailURL\"")
        buildConfigField("String", "URL_USERGUIDE", "\"$userGuideURL\"")
        buildConfigField("String", "URL_PRODUCTS", "\"$productsURL\"")
        buildConfigField("String", "URL_RESETPASSWORD", "\"$resetPasswordURL\"")
        buildConfigField("String", "URL_ORDER", "\"$orderURL\"")
        buildConfigField("String", "URL_ADDRESS", "\"$addressURL\"")
        buildConfigField("String", "URL_PAYMENT", "\"$paymentURL\"")
        buildConfigField("String", "URL_ORDER_ITEMS", "\"$orderItemsURL\"")

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
    implementation (libs.picasso)
    implementation(libs.json)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.gson)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}