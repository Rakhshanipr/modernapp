
plugins {
    alias(libs.plugins.nowinandroid.android.library)
    alias(libs.plugins.nowinandroid.android.hilt)
}

android {
    namespace = "com.google.samples.apps.nowinandroid.core.notifications"
}

dependencies {
    api(projects.core.model)

    implementation(projects.core.common)

    compileOnly(platform(libs.androidx.compose.bom))
    compileOnly(libs.androidx.compose.runtime)
}
