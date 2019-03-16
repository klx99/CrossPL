android {
    defaultConfig {
        ndk {
            abiFilters("armeabi-v7a", "arm64-v8a", "x86_64")
        }

    }
    externalNativeBuild {
        cmake {
            setPath("CMakeLists.txt")
        }
    }
}

tasks {
    val cleanNativeBuild by creating (Delete::class) {
        delete(".externalNativeBuild")
    }
    clean {
        dependsOn(cleanNativeBuild)
    }
}
