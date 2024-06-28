

plugins {
    alias(libs.plugins.nowinandroid.android.feature)
    alias(libs.plugins.nowinandroid.android.library.compose)
    alias(libs.plugins.nowinandroid.android.library.jacoco)
}

android {
    namespace = "com.google.samples.apps.nowinandroid.feature.settings"
}

dependencies {
    implementation(libs.androidx.appcompat)
    implementation(libs.google.oss.licenses)
    implementation(projects.core.data)

    testImplementation(projects.core.testing)

    androidTestImplementation(projects.core.testing)
}
