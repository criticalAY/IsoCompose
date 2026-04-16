import com.vanniktech.maven.publish.AndroidSingleVariantLibrary

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.maven.publish)
}

group = "io.github.criticalay"
version = providers.gradleProperty("kubikVersion").get()

android {
    namespace = "com.criticalay.kubik"
    compileSdk {
        version = release(36) {
            minorApiLevel = 1
        }
    }

    defaultConfig {
        minSdk = 24

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    buildFeatures {
        compose = true
    }
}

mavenPublishing {
    // Publishes the release variant with sources + javadoc jars (plugin defaults).
    configure(AndroidSingleVariantLibrary(variant = "release"))

    // Central Portal is the default host in recent plugin versions.
    publishToMavenCentral()
    signAllPublications()

    coordinates(group.toString(), "kubik", version.toString())

    pom {
        name.set("kubik")
        description.set("Isometric 3D rendering library for Jetpack Compose.")
        url.set("https://github.com/criticalAY/IsoCompose")
        licenses {
            license {
                name.set("The Apache License, Version 2.0")
                url.set("https://www.apache.org/licenses/LICENSE-2.0.txt")
            }
        }
        developers {
            developer {
                id.set("criticalay")
                name.set("Ashish Yadav")
                email.set("mailtoashish693@gmail.com")
            }
        }
        scm {
            connection.set("scm:git:git://github.com/criticalAY/IsoCompose.git")
            developerConnection.set("scm:git:ssh://github.com/criticalAY/IsoCompose.git")
            url.set("https://github.com/criticalAY/IsoCompose")
        }
    }
}

dependencies {
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.foundation)
    implementation(libs.androidx.core.ktx)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.junit)
}
