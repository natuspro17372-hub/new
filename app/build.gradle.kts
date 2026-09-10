plugins { id("com.android.application") }

android {
    namespace = "com.logo9.hmi"
    compileSdk = 35
    defaultConfig {
        applicationId = "com.logo9.hmi"
        minSdk = 26
        targetSdk = 35
        versionCode = 3
        versionName = "3.0.0-HMI-PRO"
    }
    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
}
