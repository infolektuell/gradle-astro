package de.infolektuell.gradle.astro

import org.gradle.testfixtures.ProjectBuilder
import kotlin.test.Test
import kotlin.test.assertNotNull

/**
 * A simple unit test for the 'org.example.greeting' plugin.
 */
class AstroPluginTest {
    @Test fun `plugin registers task`() {
        // Create a test project and apply the plugin
        val project = ProjectBuilder.builder().build()
        project.plugins.apply(AstroPlugin.PLUGIN_NAME)

        // Verify the result
        assertNotNull(project.tasks.findByName("astroBuild"))
    }
}
