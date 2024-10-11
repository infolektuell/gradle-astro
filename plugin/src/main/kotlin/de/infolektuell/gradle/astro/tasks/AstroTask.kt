package de.infolektuell.gradle.astro.tasks

import org.gradle.api.DefaultTask
import org.gradle.api.file.ConfigurableFileCollection
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputDirectory
import org.gradle.api.tasks.InputFiles
import org.gradle.api.tasks.OutputFile
import org.gradle.process.ExecOperations
import javax.inject.Inject

abstract class AstroTask : DefaultTask() {
    @get:Inject
    protected abstract val execOperations: ExecOperations
    @get:InputDirectory
    abstract val srcDir: DirectoryProperty
    @get:InputFiles
    abstract val configFiles: ConfigurableFileCollection
    @get:Input
    abstract val root: Property<String>
    @get:OutputFile
    abstract val report: RegularFileProperty
}
