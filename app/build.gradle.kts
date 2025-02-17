plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    id("kotlin-kapt")
    id ("kotlin-parcelize")

}

android {
    namespace = "com.sasakiappstudio.sasakiflix"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.sasakiappstudio.sasakiflix"
        minSdk = 23
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
    buildFeatures {
        compose = true
    }
}

dependencies {

    // Dependências básicas do AndroidX
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)

    // Testes
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    // Biblioteca AppCompat necessária para 'AppCompatActivity'
    implementation("androidx.appcompat:appcompat:1.6.1")

    // RecyclerView para exibir a lista de filmes
    implementation("androidx.recyclerview:recyclerview:1.2.1")

    // Retrofit para requisições HTTP
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")

    // Glide para carregar imagens dos filmes
    implementation("com.github.bumptech.glide:glide:4.15.1")
    kapt("com.github.bumptech.glide:compiler:4.15.1")

    // ViewModel e LiveData (Arquitetura MVVM)
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.1")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.6.1")

    //menu
    implementation("androidx.drawerlayout:drawerlayout:1.1.1")
    implementation("com.google.android.material:material:1.9.0")

    //Room
    implementation("androidx.room:room-runtime:2.6.1")
    kapt("androidx.room:room-compiler:2.6.1")
    implementation("androidx.room:room-ktx:2.6.1")

    // jetbrains
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.1 ")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.1")

    //generos filmes

    implementation ("com.squareup.okhttp3:okhttp:4.10.0") // OkHttp para requisições HTTP
    implementation ("com.squareup.retrofit2:retrofit:2.9.0") // Retrofit (opcional)
    implementation ("com.squareup.retrofit2:converter-gson:2.9.0") // Conversor Gson para Retrofit
    implementation ("com.google.code.gson:gson:2.8.9") // Biblioteca Gson para JSON

}
