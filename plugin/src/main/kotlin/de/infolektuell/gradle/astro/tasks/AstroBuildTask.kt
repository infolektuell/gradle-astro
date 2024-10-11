package de.infolektuell.gradle.astro.tasks

import org.gradle.api.file.DirectoryProperty
import org.gradle.api.tasks.InputDirectory
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.TaskAction
import java.nio.file.Files

abstract class AstroBuildTask : AstroTask() {
    @get:InputDirectory
    abstract val publicDir: DirectoryProperty
    @get:OutputDirectory
    abstract val buildDir: DirectoryProperty
    @TaskAction
    fun build() {
        Files.newOutputStream(report.asFile.get().toPath()).use { s ->
            execOperations.exec { spec ->
                spec.commandLine("npx", "astro", "build", "--out-dir", buildDir.asFile.get().absolutePath, "--root", root.get())
                spec.standardOutput = s
            }
        }
    }
}
