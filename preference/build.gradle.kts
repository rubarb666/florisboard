
plugins {
    id("com.android.library") version "4.2.1"
    kotlin("android") version "1.5.0"
    kotlin("kapt") version "1.5.0"
    kotlin("plugin.serialization") version "1.5.0"
}

android {
    compileSdkVersion(30)
    buildToolsVersion("30.0.3")

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_1_8.toString()
        freeCompilerArgs = listOf("-Xallow-result-return-type", "-Xopt-in=kotlin.RequiresOptIn")
    }

    defaultConfig {
        minSdkVersion(23)
        targetSdkVersion(30)
        versionCode(1)
        versionName("0.0.1")

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildFeatures {
        viewBinding = true
    }

    buildTypes {
        named("debug").configure {
            versionNameSuffix = "-debug"

            isDebuggable = true
        }

        create("beta") // Needed because by default the "beta" BuildType does not exist
        named("beta").configure {
            versionNameSuffix = "-beta03"
            proguardFiles.add(getDefaultProguardFile("proguard-android-optimize.txt"))
        }

        named("release").configure {
            proguardFiles.add(getDefaultProguardFile("proguard-android-optimize.txt"))
        }
    }

    testOptions {
        unitTests {
            isIncludeAndroidResources = true
        }
    }

    lintOptions {
        isAbortOnError = false
    }
}

dependencies {
    implementation("androidx.activity", "activity-ktx", "1.2.1")
    implementation("androidx.appcompat", "appcompat", "1.2.0")
    implementation("androidx.autofill", "autofill", "1.1.0")
    implementation("androidx.core", "core-ktx", "1.3.2")
    implementation("androidx.fragment", "fragment-ktx", "1.3.0")
    implementation("androidx.preference", "preference-ktx", "1.1.1")
    implementation("androidx.constraintlayout", "constraintlayout", "2.0.4")
    implementation("androidx.lifecycle", "lifecycle-service", "2.2.0")
    implementation("com.google.android", "flexbox", "2.0.1")
    implementation("com.google.android.material", "material", "1.3.0")
    implementation("org.jetbrains.kotlinx", "kotlinx-coroutines-android", "1.4.2")
    implementation("org.jetbrains.kotlinx", "kotlinx-serialization-json", "1.1.0")
    implementation("com.jaredrummler", "colorpicker", "1.1.0")
    implementation("com.nambimobile.widgets", "expandable-fab", "1.0.2")

    testImplementation("junit", "junit", "4.13.1")
    testImplementation("org.mockito", "mockito-inline", "3.7.7")
    testImplementation("org.robolectric", "robolectric", "4.5.1")
    androidTestImplementation("androidx.test.ext", "junit", "1.1.2")
    androidTestImplementation("androidx.test.espresso", "espresso-core", "3.3.0")
}
