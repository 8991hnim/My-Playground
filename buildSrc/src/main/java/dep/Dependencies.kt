package dep

/**
 * @author 89hnim
 * @since 29/07/2021
 */
object Versions {
    const val compileSdkVersion = 30
    const val buildToolsVersion = "30.0.3"
    const val minSdkVersion = 21
    const val targetSdkVersion = 30

    object Release {
        const val versionCode = 1
        const val versionName = "1.0"
    }
}

object Fundamentals {

    object AndroidX {
        const val core = "androidx.core:core-ktx:1.6.0"
        const val appcompat = "androidx.appcompat:appcompat:1.3.1"
        const val activityKtx = "androidx.activity:activity-ktx:1.2.4"
        const val fragmentKtx = "androidx.fragment:fragment-ktx:1.3.6"
    }

    object Ui {
        const val material = "com.google.android.material:material:1.4.0"
        const val constraintLayout = "androidx.constraintlayout:constraintlayout:2.0.4"
    }

    object Kotlin {
        private const val kotlin_version = "1.5.21"
        const val stdLib = "org.jetbrains.kotlin:kotlin-stdlib:$kotlin_version"
    }

}

object Modules {

    const val testRepo = ":test-repo"
    const val navigation = ":navigation"

    object Features {
        const val collapseTextView = ":custom-view:collapse-text-view"
        const val circularProgressButton = ":custom-view:circular-progress-button"
        const val polygonImageView = ":custom-view:polygon-image-view"
    }

}

object Libraries {

    object DeepLink {
        private const val deeplink_version = "5.4.3"
        const val dispatch = "com.airbnb:deeplinkdispatch:$deeplink_version"
        const val processor = "com.airbnb:deeplinkdispatch-processor:$deeplink_version"
    }

    object Glide {
        private const val glide_version = "4.12.0"
        const val core = "com.github.bumptech.glide:glide:$glide_version"
        const val processor = "com.github.bumptech.glide:compiler:$glide_version"
    }

}
