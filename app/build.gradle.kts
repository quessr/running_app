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
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.running_app"
        minSdk = 24
        targetSdk = 34
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

    // RecyclerView
    implementation ("androidx.recyclerview:recyclerview:1.3.2")
    // CardView
    implementation ("androidx.cardview:cardview:1.0.0")

    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.9.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")

    //room
    implementation("androidx.room:room-runtime:2.6.0")

    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

    // data binding
    implementation(platform("org.jetbrains.kotlin:kotlin-bom:1.8.0"))


    //구글맵
    implementation("com.google.android.gms:play-services-maps:18.0.2")
    implementation("com.google.android.gms:play-services-location:19.0.1")

    // Notification
    implementation("androidx.core:core:1.0.0")

    annotationProcessor("androidx.room:room-compiler:2.6.0")

}