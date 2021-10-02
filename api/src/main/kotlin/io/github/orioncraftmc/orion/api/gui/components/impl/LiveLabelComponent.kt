package io.github.orioncraftmc.orion.api.gui.components.impl

class LiveLabelComponent(var textFetcher: () -> String) : LabelComponent() {
	@Suppress("UNUSED_PARAMETER")
	override var text: String
		get() = textFetcher()
		set(value) {}
}
