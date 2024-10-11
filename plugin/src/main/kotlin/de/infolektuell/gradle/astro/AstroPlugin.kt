package de.infolektuell.gradle.astro

import de.infolektuell.gradle.astro.tasks.AstroBuildTask
import de.infolektuell.gradle.astro.tasks.AstroCheckTask
import de.infolektuell.gradle.astro.tasks.AstroTask
import de.infolektuell.gradle.astro.tasks.NpmInstallTask
import org.gradle.api.Plugin
import org.gradle.api.Project

abstract class AstroPlugin : Plugin<Project> {
  override fun apply(project: Project) {
    val srcDir = project.layout.projectDirectory.dir("src")
    val publicDir = project.layout.projectDirectory.dir("public")
    val buildDir = project.layout.buildDirectory.dir("dist")
    val logDir = project.layout.buildDirectory.dir("reports/astro")
    val configFiles = project.layout.projectDirectory.files("package.json", "package-lock.json", "tsconfig.json", "astro.config.mjs", "svelte.config.js")
    val npmInstallTask = project.tasks.register("npmInstall", NpmInstallTask::class.java) { task ->
        task.configFiles.from(project.layout.projectDirectory.files("package.json", "package-lock.json"))
        task.packages.convention(project.layout.projectDirectory.dir("node_modules"))
        task.ci.convention(project.providers.environmentVariable("CI").orElse("").map { it.isNotEmpty() })
    }
      project.tasks.withType(AstroTask::class.java).configureEach { task ->
      task.srcDir.convention(srcDir)
      task.configFiles.from(configFiles)
      task.root.convention(project.layout.projectDirectory.asFile.absolutePath)
          task.inputs.dir(npmInstallTask.flatMap { it.packages })
    }
      val astroBuildTask = project.tasks.register("astroBuild", AstroBuildTask::class.java) { task ->
          task.publicDir.convention(publicDir)
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
