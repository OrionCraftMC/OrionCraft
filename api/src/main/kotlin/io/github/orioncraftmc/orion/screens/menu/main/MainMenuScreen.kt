/*
 * MIT License
 *
 * Copyright (c) 2021 OrionCraftMC
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package io.github.orioncraftmc.orion.screens.menu.main

import io.github.orioncraftmc.components.flex
import io.github.orioncraftmc.components.model.Anchor
import io.github.orioncraftmc.components.model.Padding
import io.github.orioncraftmc.components.model.Size
import io.github.orioncraftmc.meditate.enums.YogaAlign
import io.github.orioncraftmc.meditate.enums.YogaJustify
import io.github.orioncraftmc.orion.api.OrionCraftConstants
import io.github.orioncraftmc.orion.api.event.EventBus
import io.github.orioncraftmc.orion.api.event.impl.action.GameActionChangeEvent
import io.github.orioncraftmc.orion.api.event.impl.action.GameActionType
import io.github.orioncraftmc.orion.api.gui.components.impl.LabelComponent
import io.github.orioncraftmc.orion.api.gui.components.screens.ComponentOrionScreen
import io.github.orioncraftmc.orion.screens.menu.main.components.MainMenuButtonContainerComponent

const val buttonGapSize = 7.5f
const val buttonGapDoubleSize = buttonGapSize * 2
val menuButtonSize = Size(215.0, 20.0)

abstract class MainMenuScreen : ComponentOrionScreen(true) {

	private val menuButtonContainer = MainMenuButtonContainerComponent()

	init {
		flex {
			justifyContent = YogaJustify.CENTER
			alignItems = YogaAlign.CENTER
		}
		EventBus.callEvent(GameActionChangeEvent(GameActionType.MAIN_MENU))
		padding = Padding(5.0)
		addLabels()
	}

	override fun onResize() {
		super.onResize()
	}

	private fun addLabels() {
		addComponent(LabelComponent(OrionCraftConstants.mainMenuClientName).apply {
			anchor = Anchor.BOTTOM_LEFT
		})

		addComponent(LabelComponent("Copyright Mojang Studios. Do not distribute!").apply {
			anchor = Anchor.BOTTOM_RIGHT
		})

		addComponent(menuButtonContainer)
	}


	// Implementation is provided by version
	abstract fun renderSkybox(mouseX: Int, mouseY: Int, renderPartialTicks: Float)

	// Implementation is provided by version
	abstract fun superHandleMouseClick(mouseX: Int, mouseY: Int, clickedButtonId: Int)

	// Implementation is provided by version
	abstract fun superDrawScreen(mouseX: Int, mouseY: Int, renderPartialTicks: Float)

	override fun drawScreen(mouseX: Int, mouseY: Int, renderPartialTicks: Float) {
		renderSkybox(mouseX, mouseY, renderPartialTicks)

		super.drawScreen(mouseX, mouseY, renderPartialTicks)
	}

	override fun handleMouseClick(mouseX: Int, mouseY: Int, clickedButtonId: Int) {
		super.handleMouseClick(mouseX, mouseY, clickedButtonId)
	}
}
