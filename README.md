# Gradle Astro Plugin

[Astro] is a static site generator running in JavaScript environments like Node.js.
This Gradle plugin can be used to build and check Astro sites instead of running Astro in npm scripts.

## Features

- [x] Build and check astro sites in a Gradle project
- [x] Build only if sources have changed, no extra special mode needed
- [x] Compatible with [Configuration Cache]

## Usage

In _build.gradle.kts_ next to _src_, the plugin must be configured:

```gradle kotlin dsl
plugins {
    // Good to have some standard tasks like clean
    id("base")
    // Apply the Astro plugin
    id("de.infolektuell.astro") version "0.1.0"
}
```

The plugin attaches its tasks to the base plugin's assemble and check tasks.

- `gradlew build` generates the website under _build/dist_
- `gradlew check` checks for errors in the source code
- Both don't write the command output to stdout, but the logs are written to _build/reports/astro_

## License

[MIT License](LICENSE.txt)

[astro]: https://astro.build/
[configuration cache]: https://docs.gradle.org/current/userguide/configuration_cache.html
