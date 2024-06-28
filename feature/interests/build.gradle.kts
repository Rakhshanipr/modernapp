

plugins {
    alias(libs.plugins.nowinandroid.android.feature)
    alias(libs.plugins.nowinandroid.android.library.compose)
    alias(libs.plugins.nowinandroid.android.library.jacoco)
}
android {
    namespace = "com.google.samples.apps.nowinandroid.feature.interests"
}

dependencies {
    implementation(projects.core.data)
    implementation(projects.core.domain)

    testImplementation(projects.core.testing)

    androidTestImplementation(projects.core.testing)
}
