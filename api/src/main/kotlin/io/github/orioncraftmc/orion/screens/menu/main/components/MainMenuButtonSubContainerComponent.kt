package io.github.orioncraftmc.orion.screens.menu.main.components

import io.github.orioncraftmc.meditate.enums.YogaAlign
import io.github.orioncraftmc.meditate.enums.YogaEdge
import io.github.orioncraftmc.meditate.enums.YogaFlexDirection
import io.github.orioncraftmc.meditate.enums.YogaJustify
import io.github.orioncraftmc.orion.api.bridge.minecraft.menu.main.MainMenuAction
import io.github.orioncraftmc.orion.api.gui.components.Component
import io.github.orioncraftmc.orion.api.gui.components.flex
import io.github.orioncraftmc.orion.api.gui.components.impl.containers.ComponentContainer
import io.github.orioncraftmc.orion.screens.buttonGapSize

open class MainMenuButtonSubContainerComponent(direction: YogaFlexDirection) : ComponentContainer() {
	init {
		flex {
			justifyContent = YogaJustify.CENTER
			alignItems = YogaAlign.CENTER
			flexDirection = direction
		}
	}

	fun addButton(action: MainMenuAction): MainMenuButtonComponent {
		return MainMenuButtonComponent(action).also { addComponent(it) }
	}

	fun addButtonWithFlexGrowthAndMargin(
        mainMenuAction: MainMenuAction,
        growth: Float,
        marginEdge: YogaEdge
	): Component {
		return addButton(mainMenuAction).flex {
			flexGrow = growth
			setWidth(Float.NaN)
			setMargin(marginEdge, buttonGapSize / 2)
		}
	}
}
