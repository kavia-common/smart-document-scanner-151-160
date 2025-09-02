androidApplication {
    namespace = "com.camscanner.app"

    // Keep declarative DSL minimal; only dependencies are declared here.

    dependencies {
        implementation("androidx.core:core-ktx:1.13.1")
        implementation("androidx.appcompat:appcompat:1.7.0")
        implementation("com.google.android.material:material:1.12.0")
        implementation("androidx.constraintlayout:constraintlayout:2.1.4")
        implementation("androidx.recyclerview:recyclerview:1.3.2")
        implementation("androidx.cardview:cardview:1.0.0")

        implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.8.4")
        implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.8.4")
        implementation("androidx.activity:activity-ktx:1.9.1")
        implementation("androidx.fragment:fragment-ktx:1.8.2")
        implementation("androidx.navigation:navigation-fragment-ktx:2.7.7")
        implementation("androidx.navigation:navigation-ui-ktx:2.7.7")

        // CameraX
        implementation("androidx.camera:camera-core:1.3.4")
        implementation("androidx.camera:camera-camera2:1.3.4")
        implementation("androidx.camera:camera-lifecycle:1.3.4")
        implementation("androidx.camera:camera-view:1.3.4")

        // ML Kit On-Device Text Recognition
        implementation("com.google.mlkit:text-recognition:16.0.1")

        // Room (runtime only in this minimal DSL)
        implementation("androidx.room:room-runtime:2.6.1")
        implementation("androidx.room:room-ktx:2.6.1")

        // WorkManager
        implementation("androidx.work:work-runtime-ktx:2.9.0")

        // PDF helper
        implementation("com.itextpdf:itextg:5.5.10")

        // Image helpers
        implementation("com.github.bumptech.glide:glide:4.16.0")
        implementation("jp.co.cyberagent.android:gpuimage:2.1.0")

        // Coroutines
        implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.8.1")
    }
}
