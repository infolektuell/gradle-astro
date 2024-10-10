package de.infolektuell.gradle.astro.tasks

import org.gradle.api.DefaultTask
import org.gradle.api.file.ConfigurableFileCollection
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.provider.Property
import org.gradle.api.tasks.*
import org.gradle.process.ExecOperations
import javax.inject.Inject

abstract class NpmInstallTask : DefaultTask() {
    @get:Inject
    protected abstract val execOperations: ExecOperations
    @get:InputFiles
    abstract val configFiles: ConfigurableFileCollection
    @get:Input
    abstract val ci: Property<Boolean>
    @get:OutputDirectory
    abstract val packages: DirectoryProperty
    @TaskAction
    fun install() {
        execOperations.exec { spec ->
            spec.commandLine("npm", if (ci.get()) "ci" else "i")
        }
    }
}
