
plugins {
    alias(libs.plugins.nowinandroid.android.library)
    alias(libs.plugins.nowinandroid.android.library.compose)
    alias(libs.plugins.nowinandroid.android.hilt)
}

android {
    namespace = "com.google.samples.apps.nowinandroid.core.screenshottesting"
}

dependencies {
    api(libs.roborazzi)
    implementation(libs.androidx.compose.ui.test)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.compose.ui.test)
    implementation(libs.robolectric)
    implementation(projects.core.common)
    implementation(projects.core.designsystem)
}
