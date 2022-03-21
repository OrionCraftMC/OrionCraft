rootProject.name = "OrionCraft"
include("api")
include("mods")

val proprietaryModsProjectDir = File("..", "orioncraft-proprietary-mods")
if (proprietaryModsProjectDir.exists()) {
    include(":orioncraft-proprietary-mods")
    project(":orioncraft-proprietary-mods").projectDir = proprietaryModsProjectDir
}
