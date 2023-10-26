import java.util.Properties
import java.io.FileInputStream

plugins {
    id("com.android.application")
}

val localProperties = Properties()
val localPropertiesFile = FileInputStream(rootProject.file("local.properties"))
localProperties.load(localPropertiesFile)
localPropertiesFile.close()

val googleMapApiKey = localProperties.getProperty("GOOGLE_MAP_API_KEY")

android {
    namespace = "com.example.running_app"
    compileSdk = 33

    defaultConfig {
        applicationId = "com.example.running_app"
        minSdk = 24
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

//        buildConfigField("String", "GOOGLE_MAP_API_KEY", "\"${localProperties.getProperty("GOOGLE_MAP_API_KEY")}\"")
        buildConfigField("String", "GOOGLE_MAP_API_KEY", "\"${googleMapApiKey}\"")

        manifestPlaceholders["GOOGLE_MAP_API_KEY"] = googleMapApiKey
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    dataBinding {
        enable = true
    }

    viewBinding {
        enable = true
    }

    buildFeatures {
        buildConfig = true
    }
}

dependencies {

    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.9.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

    // data binding
    implementation(platform("org.jetbrains.kotlin:kotlin-bom:1.8.0"))
}