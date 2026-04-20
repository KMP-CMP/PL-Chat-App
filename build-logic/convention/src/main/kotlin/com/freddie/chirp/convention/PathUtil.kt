package com.freddie.chirp.convention

import org.gradle.api.Project
import java.util.Locale

fun Project.pathToPackageName(): String {
    val relativePackageName = path
        .replace(":", ".")
        .lowercase()
    return "com.freddie$relativePackageName"
}

fun Project.pathToResourcePrefix(): String = path
    .replace(":", "_")
    .lowercase()
    .drop(1) + "_"

fun Project.pathToFrameworkName(): String {
    val parts = this.path.split(":", "-", "_", " ")
    // :core:data -> ["Core", "Data"]
    val result = parts.joinToString("") { part ->
        part.replaceFirstChar {
            it.titlecase(Locale.ROOT)
        }
    }
    return result
}