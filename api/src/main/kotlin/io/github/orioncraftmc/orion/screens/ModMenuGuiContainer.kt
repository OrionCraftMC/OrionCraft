package io.github.orioncraftmc.orion.screens

import io.github.orioncraftmc.meditate.enums.YogaEdge
import io.github.orioncraftmc.orion.api.gui.components.flex
import io.github.orioncraftmc.orion.api.gui.components.impl.ButtonComponent
import io.github.orioncraftmc.orion.api.gui.components.impl.containers.ComponentContainer
import io.github.orioncraftmc.orion.api.gui.components.nodeSize
import io.github.orioncraftmc.orion.api.gui.model.Size
import io.github.orioncraftmc.orion.utils.ColorConstants

class ModMenuGuiContainer : ComponentContainer() {
	init {
		flex {
			setWidthPercent(70f)
			setHeightAuto()
			aspectRatio = 1.75f
		}

		addComponent(ButtonComponent("X", onClick = { println("TODO: Implement closing") }).flex {
			nodeSize = Size(10.0)
			setMarginAuto(YogaEdge.LEFT)
			setMargin(YogaEdge.TOP, 5f)
			setMargin(YogaEdge.RIGHT, 5f)
		})

		backgroundColor = ColorConstants.modLabelBackgroundColor
	}
}
