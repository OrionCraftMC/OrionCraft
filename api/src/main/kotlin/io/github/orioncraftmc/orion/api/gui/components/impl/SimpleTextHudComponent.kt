package io.github.orioncraftmc.orion.api.gui.components.impl

import io.github.orioncraftmc.components.containers.ComponentContainer
import io.github.orioncraftmc.components.model.Anchor
import io.github.orioncraftmc.components.model.Size
import io.github.orioncraftmc.orion.utils.ColorConstants

class SimpleTextHudComponent(function: () -> String) : ComponentContainer() {

	val textComponent = LiveLabelComponent(function).apply {
		anchor = Anchor.MIDDLE
	}

	init {
		addComponent(textComponent)
		size = Size(58.0, 19.0)
		backgroundColor = ColorConstants.modLabelBackgroundColor
	}

	override fun toString(): String {
		return textComponent.text
	}
}
