package de.infolektuell.gradle.astro.tasks

import org.gradle.api.tasks.TaskAction
import org.gradle.api.tasks.VerificationTask
import java.nio.file.Files

abstract class AstroCheckTask : AstroTask(), VerificationTask {
    @TaskAction
    fun check() {
        Files.newOutputStream(report.asFile.get().toPath()).use { s ->
            execOperations.exec { spec ->
                spec.commandLine("npx", "astro", "check", "--root", root.get())
                spec.standardOutput = s
            }
        }
    }
}
