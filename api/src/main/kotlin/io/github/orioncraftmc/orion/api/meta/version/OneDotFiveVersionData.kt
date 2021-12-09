package io.github.orioncraftmc.orion.api.meta.version

object OneDotFiveVersionData : AbstractVersionData() {

	// 1.5.2 has the vanilla F5 keybind hardcoded in the main loop
	override val hasVanillaPerspectiveKeybinding: Boolean
		get() = false

}
