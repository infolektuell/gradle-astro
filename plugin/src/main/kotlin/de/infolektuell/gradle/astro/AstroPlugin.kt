package de.infolektuell.gradle.astro

import org.gradle.api.DefaultTask
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.file.ConfigurableFileCollection
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.provider.Property
import org.gradle.api.tasks.*
import org.gradle.process.ExecOperations
import java.nio.file.Files
import javax.inject.Inject

abstract class AstroTask : DefaultTask() {
  @get:Inject
  protected abstract val execOperations: ExecOperations
  @get:InputDirectory
  abstract val srcDir: DirectoryProperty
  @get:InputDirectory
  abstract val publicDir: DirectoryProperty
  @get:InputFiles
  abstract val configFiles: ConfigurableFileCollection
  @get:Input
  abstract val root: Property<String>
  @get:OutputFile
  abstract val report: RegularFileProperty
}

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

abstract class AstroBuildTask : AstroTask() {
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

abstract class AstroPlugin : Plugin<Project> {
  override fun apply(project: Project) {
    val srcDir = project.layout.projectDirectory.dir("src")
    val publicDir = project.layout.projectDirectory.dir("public")
    val buildDir = project.layout.buildDirectory.dir("dist")
    val logDir = project.layout.buildDirectory.dir("reports/astro")
    val configFiles = project.layout.projectDirectory.files("package.json", "package-lock.json", "tsconfig.json", "astro.config.mjs", "svelte.config.js")
    project.tasks.withType(AstroTask::class.java).configureEach { task ->
      task.srcDir.convention(srcDir)
      task.publicDir.convention(publicDir)
      task.configFiles.from(configFiles)
      task.root.convention(project.layout.projectDirectory.asFile.absolutePath)
    }
    val astroBuildTask = project.tasks.register("astroBuild", AstroBuildTask::class.java) { task ->
      task.buildDir.convention(buildDir)
      task.report.convention(logDir.map { it.file("build.log") })
    }
    project.tasks.findByName("assemble")?.dependsOn(astroBuildTask)

    val astroCheckTask = project.tasks.register("astroCheck", AstroCheckTask::class.java) { task ->
      task.report.convention(logDir.map { it.file("check.log") })
    }
    project.tasks.findByName("check")?.dependsOn(astroCheckTask)
  }
  companion object {
    const val PLUGIN_NAME = "de.infolektuell.astro"
  }
}
